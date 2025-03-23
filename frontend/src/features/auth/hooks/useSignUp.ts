import type { AuthCredentials } from "@/features/auth/schemas/credentials-schema";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";

import { ApiRoutes } from "@/config/api-routes";
import { api } from "@/lib/api";

export function useSignUp() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: async (data: AuthCredentials) => {
      const result = await api.post(ApiRoutes.signUp, data);

      if (result.status === 200) {
        return navigate({
          to: "/sign-in",
          search: {
            newAccount: true,
          },
        });
      }
    },
  });
}
