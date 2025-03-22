import { AuthForm } from "@/features/auth/components/form";
import { AuthHeading } from "@/features/auth/components/heading";
import { AuthInformativeText } from "@/features/auth/components/informative-text";
import { useSignUp } from "@/features/auth/hooks/useSignUp";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/sign-up")({
	component: SignUpPage,
});

function SignUpPage() {
	const { isPending, mutate, error } = useSignUp();

	return (
		<>
			<AuthHeading
				heading="Create an account"
				description="Provide your details to create an account."
			/>
			<AuthForm
				submitText="Sign up"
				onSubmit={mutate}
				error={error}
				isPending={isPending}
			/>
			<AuthInformativeText
				text="Already have an account?"
				linkText="Sign in"
				linkTo="/sign-in"
			/>
		</>
	);
}
