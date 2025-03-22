import type { AuthCredentials } from "@/features/auth/schemas/credentials-schema";
import { useAuth } from "@/lib/auth";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";

export function useSignIn() {
	const navigate = useNavigate();
	const auth = useAuth();

	return useMutation({
		mutationFn: async (data: AuthCredentials) => {
			const response =
				data.username === "test" && data.password === "testtest"
					? ({ status: 200, data: "its-my-mocked-access-token" } as const)
					: ({ status: 400, data: null } as const);

			// sleep 4 seconds to simulate a network request
			await new Promise((resolve) => setTimeout(resolve, 2_000));

			if (response.status === 200) {
				auth.login(response.data);

				return navigate({
					to: "/dashboard",
				});
			}

			return Promise.reject(new Error("Sign up failed"));
		},
	});
}
