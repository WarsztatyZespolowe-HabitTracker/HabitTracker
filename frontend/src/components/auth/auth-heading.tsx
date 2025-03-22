interface AuthHeadingProps {
	heading: string;
	description: string;
}
export function AuthHeading({ heading, description }: AuthHeadingProps) {
	return (
		<div className="space-y-2 text-left">
			<h1 className="text-2xl md:text-3xl lg:text-4xl font-bold">{heading}</h1>
			<span className="lg:text-lg text-muted-foreground">{description}</span>
		</div>
	);
}
