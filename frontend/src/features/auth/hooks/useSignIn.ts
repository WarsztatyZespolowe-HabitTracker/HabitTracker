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
      const response = await fetch(
          `${import.meta.env.VITE_API_URL}/api/auth/login`,
          {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(data),
          }
      );

      if (!response.ok) {
          throw new Error("Login failed");
      }

      const userData = await response.json();
      
      const tokenWithRole = JSON.stringify({
          ...data,
          role: userData.role || "USER",
      });
      
      auth.login(tokenWithRole);

      return navigate({
        to: "/habits",
      });
    },
  });
}
