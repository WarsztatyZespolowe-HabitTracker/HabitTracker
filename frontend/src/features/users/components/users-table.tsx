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

export function UsersTable() {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Username</TableHead>
          <TableHead className="w-44">Role</TableHead>
          <TableHead className="w-44">Created at</TableHead>
          <TableHead className="w-16">Delete</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow>
          <TableCell className="font-medium">MyAdminAccount</TableCell>
          <TableCell>22-02-2024</TableCell>
          <TableCell>Admin</TableCell>
          <TableCell className="text-center">
            <Button variant="destructive" size="icon">
              <TrashIcon />
              <span className="sr-only">Delete</span>
            </Button>
          </TableCell>
        </TableRow>
        <TableRow>
          <TableCell className="font-medium">MyTestingAccount</TableCell>
          <TableCell>23-03-2024</TableCell>
          <TableCell>User</TableCell>
          <TableCell className="text-center">
            <Button variant="destructive" size="icon">
              <TrashIcon />
              <span className="sr-only">Delete</span>
            </Button>
          </TableCell>
        </TableRow>
      </TableBody>
    </Table>
  );
}
