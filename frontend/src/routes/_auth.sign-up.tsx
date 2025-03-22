import { AuthHeading } from "@/components/auth/auth-heading";
import { InformativeText } from "@/components/auth/informative-text";
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

			<InformativeText
				text="Already have an account?"
				linkText="Sign in"
				linkTo="/sign-in"
			/>
		</>
	);
}
