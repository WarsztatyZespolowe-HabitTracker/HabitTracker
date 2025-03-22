import type { AuthCredentials } from "@/features/auth/schemas/credentials-schema";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";

export function useSignUp() {
	const navigate = useNavigate();

	return useMutation({
		mutationFn: async (data: AuthCredentials) => {
			const response =
				data.username === "test" && data.password === "testtest"
					? { status: 200 }
					: { status: 400 };

			// sleep 4 seconds to simulate a network request
			await new Promise((resolve) => setTimeout(resolve, 2_000));

			if (response.status === 200) {
				return navigate({
					to: "/sign-in",
					search: {
						newAccount: true,
					},
				});
			}

			return Promise.reject(new Error("Sign up failed"));
		},
	});
}
