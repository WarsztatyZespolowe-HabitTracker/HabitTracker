export const ApiRoutes = {
	signIn: "/api/auth/login",
	signUp: "/api/auth/register",
} as const satisfies Record<string, `/api/${string}`>;

export type ApiRoute = (typeof ApiRoutes)[keyof typeof ApiRoutes];
