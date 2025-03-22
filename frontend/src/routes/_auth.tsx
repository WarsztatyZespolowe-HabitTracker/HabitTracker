import { Logo } from "@/components/ui/logo";
import { Outlet, createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
	component: AuthLayout,
});

function AuthLayout() {
	return (
		<div className="flex min-h-screen items-center flex-col">
			<Logo asLink className="my-2" />
			<div className="flex-1 md:grid md:place-content-center p-4">
				<main className="flex flex-col items-center w-full max-w-md space-y-4">
					<Outlet />
				</main>
			</div>
		</div>
	);
}
