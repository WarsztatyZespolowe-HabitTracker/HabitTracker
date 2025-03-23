import { Alert, AlertDescription } from "@/components/ui/alert";
import { AuthForm } from "@/features/auth/components/form";
import { AuthHeading } from "@/features/auth/components/heading";
import { AuthInformativeText } from "@/features/auth/components/informative-text";
import { useSignIn } from "@/features/auth/hooks/useSignIn";
import { createFileRoute } from "@tanstack/react-router";
import { zodValidator } from "@tanstack/zod-adapter";
import { z } from "zod";

const searchSchema = z.object({
  newAccount: z.boolean().optional(),
});

export const Route = createFileRoute("/_auth/sign-in")({
  validateSearch: zodValidator(searchSchema),
  component: SignInPage,
});

function SignInPage() {
  const { isPending, mutate, isError } = useSignIn();
  const { newAccount } = Route.useSearch();

  return (
    <>
      <AuthHeading
        heading="Sign in"
        description="Enter your details to sign in."
      />

      {newAccount && (
        <Alert variant="informative">
          <AlertDescription>
            Account created successfully. You can now sign in.
          </AlertDescription>
        </Alert>
      )}

      <AuthForm
        submitText="Sign in"
        onSubmit={mutate}
        isPending={isPending}
        isError={isError}
        errorMessage="Could not find an account with the provided credentials."
      />

      <AuthInformativeText
        text="Don't have an account?"
        linkText="Sign up"
        linkTo="/sign-up"
      />
    </>
  );
}
