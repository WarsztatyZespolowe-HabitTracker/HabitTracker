import { ApiRoutes } from "@/config/api-routes";
import type { AuthCredentials } from "@/features/auth/schemas/credentials-schema";
import { api } from "@/lib/api";
import { useAuth } from "@/lib/auth";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";

export function useSignIn() {
  const navigate = useNavigate();
  const auth = useAuth();

  return useMutation({
    mutationFn: async (data: AuthCredentials) => {
      await api.post(ApiRoutes.signIn, data);

      const dataAsKey = JSON.stringify(data);
      auth.login(dataAsKey);

      return navigate({
        to: "/habits",
      });
    },
  });
}
