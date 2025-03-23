import { cn } from "@/lib/utils";
import { Link } from "@tanstack/react-router";
import { CheckCheckIcon } from "lucide-react";

interface LogoProps {
	asLink?: boolean;
	className?: string;
}

export function Logo({ asLink, className }: LogoProps) {
	const Comp = asLink ? Link : "div";

	return (
		<Comp
			to="/"
			aria-label={asLink ? "Go to homepage" : undefined}
			className={cn("flex gap-2 items-center text-xl font-bold", className)}
		>
			<CheckCheckIcon className="size-6 text-primary" />
			<span>HabitTracker</span>
		</Comp>
	);
}
