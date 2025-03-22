import { AuthForm } from "@/features/auth/components/form";
import { AuthHeading } from "@/features/auth/components/heading";
import { AuthInformativeText } from "@/features/auth/components/informative-text";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/sign-in")({
	component: SignInPage,
});

function SignInPage() {
	return (
		<>
			<AuthHeading
				heading="Sign in"
				description="Enter your details to sign in."
			/>

			<AuthForm
				submitText="Sign in"
				submitLoadingText="Signing in..."
				onSubmit={console.log}
			/>

			<AuthInformativeText
				text="Don't have an account?"
				linkText="Sign up"
				linkTo="/sign-up"
			/>
		</>
	);
}
