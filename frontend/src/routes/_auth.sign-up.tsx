import { AuthForm } from "@/features/auth/components/form";
import { AuthHeading } from "@/features/auth/components/heading";
import { AuthInformativeText } from "@/features/auth/components/informative-text";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/sign-up")({
	component: SignUpPage,
});

function SignUpPage() {
	return (
		<>
			<AuthHeading
				heading="Create an account"
				description="Provide your details to create an account."
			/>
			<AuthForm
				submitText="Sign up"
				submitLoadingText="Signing up..."
				onSubmit={console.log}
			/>

			<AuthInformativeText
				text="Already have an account?"
				linkText="Sign in"
				linkTo="/sign-in"
			/>
		</>
	);
}
