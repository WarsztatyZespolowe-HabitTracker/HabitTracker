import { Outlet, createRootRouteWithContext } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import type { Auth } from "@/lib/auth";
import type { QueryClient } from "@tanstack/react-query";

export const Route = createRootRouteWithContext<{
	auth: Auth;
	queryClient: QueryClient;
}>()({
	component: Root,
});

function Root() {
	return (
		<>
			<Outlet />
			<TanStackRouterDevtools />
		</>
	);
}
