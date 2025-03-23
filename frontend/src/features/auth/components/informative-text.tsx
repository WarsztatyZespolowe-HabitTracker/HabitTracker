import type { LinkProps } from "@tanstack/react-router";
import { Link } from "@tanstack/react-router";

interface AuthInformativeTextProps {
	text: string;
	linkText: string;
	linkTo: LinkProps["to"];
}

export function AuthInformativeText({
	text,
	linkText,
	linkTo,
}: AuthInformativeTextProps) {
	return (
		<span>
			{text}{" "}
			<Link to={linkTo} className="underline underline-offset-2 font-medium">
				{linkText}
			</Link>
		</span>
	);
}
