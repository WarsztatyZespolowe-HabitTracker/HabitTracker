import { Button } from "@/components/ui/button";
import { Logo } from "@/components/ui/logo";
import { Link } from "@tanstack/react-router";

export function Header() {
	return (
		<header className="sticky top-0 z-40 w-full border-b">
			<div className="container mx-auto flex items-center justify-between py-2 px-4">
				<Logo />
				<nav className="flex items-center space-x-4">
					<Button variant="outline" asChild>
						<Link to="/sign-in">Sign in</Link>
					</Button>
					<Button className="hidden md:block" asChild>
						<Link to="/sign-up">Sign up</Link>
					</Button>
				</nav>
			</div>
		</header>
	);
}
