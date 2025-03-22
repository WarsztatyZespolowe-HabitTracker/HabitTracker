import { Button, type ButtonProps } from "@/components/ui/button";
import { useAuth } from "@/lib/auth";
import { useNavigate } from "@tanstack/react-router";
import { useTransition } from "react";

interface SignOutButtonProps extends Omit<ButtonProps, "onClick"> {}

export function SignOutButton(props: SignOutButtonProps) {
	const [isPending, startTransition] = useTransition();
	const navigate = useNavigate();
	const auth = useAuth();

	const signOutHandler = () => {
		startTransition(() => {
			auth.logout();
			navigate({
				to: "/",
			});
		});
	};

	return <Button isLoading={isPending} onClick={signOutHandler} {...props} />;
}
