import type { LinkProps } from "@tanstack/react-router";
import { Link } from "@tanstack/react-router";

interface InformativeTextProps {
	text: string;
	linkText: string;
	linkTo: LinkProps["to"];
}

export function InformativeText({
	text,
	linkText,
	linkTo,
}: InformativeTextProps) {
	return (
		<span className="space-x-24">
			{text}{" "}
			<Link to={linkTo} className="underline underline-offset-2 font-medium">
				{linkText}
			</Link>
		</span>
	);
}
