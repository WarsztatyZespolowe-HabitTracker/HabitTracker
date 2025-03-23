import { Logo } from "@/components/ui/logo";
import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  beforeLoad: ({ context }) => {
    if (context.auth.isAuthenticated) {
      throw redirect({
        to: "/habits",
      });
    }
  },
  component: AuthLayout,
});

function AuthLayout() {
  return (
    <div className="flex min-h-screen items-center flex-col">
      <Logo asLink to="/" className="my-2" />
      <div className="flex-1 flex items-center p-4 w-full">
        <main className="flex flex-col items-center w-full max-w-sm mx-auto space-y-4">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
