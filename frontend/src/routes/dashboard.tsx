import { SignOutButton } from "@/features/auth/components/sign-up-button";
import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/dashboard")({
	beforeLoad: ({ context }) => {
		if (!context.auth.isAuthenticated) {
			throw redirect({
				to: "/sign-in",
			});
		}
	},
	component: DashboardPage,
});

function DashboardPage() {
	return (
		<div className="min-h-svh w-full grid p-4 place-content-center">
			<SignOutButton>Sign out</SignOutButton>
		</div>
	);
}
