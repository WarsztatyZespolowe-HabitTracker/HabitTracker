import { AuthHeading } from "@/components/auth/auth-heading";
import { InformativeText } from "@/components/auth/informative-text";
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

			<InformativeText
				text="Don't have an account?"
				linkText="Sign up"
				linkTo="/sign-up"
			/>
		</>
	);
}
