import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { TrashIcon } from "lucide-react";
import { useEffect, useState } from "react";
import { useAuth } from "@/lib/auth";

interface User {
  id: string;
  username: string;
  role: string;
  active: boolean;
  createdAt?: string;
}

export function UsersTable() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuth();

  useEffect(() => {
    async function fetchUsers() {
      if (!token) return;

      try {
        setLoading(true);
        setError(null);
        const tokenObj = JSON.parse(token);
        const encodedAuth = btoa(`${tokenObj.username}:${tokenObj.password}`);

        const response = await fetch(
          `${import.meta.env.VITE_API_URL}/api/admin/users`,
          {
            headers: {
              Authorization: `Basic ${encodedAuth}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error(`Failed to fetch users: ${response.statusText}`);
        }

        const data = await response.json();
        setUsers(data);
      } catch (err) {
        console.error("Error fetching users:", err);
        setError(err instanceof Error ? err.message : "An error occurred");
      } finally {
        setLoading(false);
      }
    }

    fetchUsers();
  }, [token]);

  async function handleDeleteUser(userId: string) {
    if (
      !token ||
      !window.confirm("Are you sure you want to delete this user?")
    )
      return;

    try {
      const tokenObj = JSON.parse(token);
      const encodedAuth = btoa(`${tokenObj.username}:${tokenObj.password}`);

      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/api/admin/users/${userId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Basic ${encodedAuth}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`Failed to delete user: ${response.statusText}`);
      }

      setUsers(users.filter((user) => user.id !== userId));
    } catch (err) {
      console.error("Error deleting user:", err);
      alert("Failed to delete user.");
    }
  }

  if (loading) {
    return <div className="text-center p-4">Loading users...</div>;
  }

  if (error) {
    return <div className="text-red-500 p-4">Error: {error}</div>;
  }

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Username</TableHead>
          <TableHead className="w-44">Role</TableHead>
          <TableHead className="w-44">Status</TableHead>
          <TableHead className="w-16">Delete</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {users.length === 0 ? (
          <TableRow>
            <TableCell colSpan={4} className="text-center py-4">
              No users found
            </TableCell>
          </TableRow>
        ) : (
          users.map((user) => (
            <TableRow key={user.id}>
              <TableCell className="font-medium">{user.username}</TableCell>
              <TableCell>{user.role}</TableCell>
              <TableCell>
                <span
                  className={
                    user.active ? "text-green-600" : "text-red-600"
                  }
                >
                  {user.active ? "Active" : "Inactive"}
                </span>
              </TableCell>
              <TableCell className="text-center">
                <Button
                  variant="destructive"
                  size="icon"
                  onClick={() => handleDeleteUser(user.id)}
                >
                  <TrashIcon />
                  <span className="sr-only">Delete</span>
                </Button>
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
    </Table>
  );
}
