import { UsersTable } from "@/features/users/components/users-table";
import { createFileRoute, Navigate } from "@tanstack/react-router";
import { useIsAdmin } from '@/features/auth/hooks/useIsAdmin';

export const Route = createFileRoute("/_dashboard/users")({
  component: UsersPage,
});

function UsersPage() {
  const { isAdmin } = useIsAdmin();

  if (!isAdmin) {
    return <Navigate to="/habits" />;
  }

  return (
    <>
      <div className="spacy-y-2">
        <h1 className="text-3xl font-semibold">User Management</h1>
        <p className="text-muted-foreground">
          Manage users in your application
        </p>
      </div>
      <div className="border border-border rounded-sm p-4">
        <UsersTable />
      </div>
    </>
  );
}
