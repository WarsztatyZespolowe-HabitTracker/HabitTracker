import { Button } from "@/components/ui/button";
import { Link } from "@tanstack/react-router";
import { ArrowRightIcon } from "lucide-react";

export function Hero() {
	return (
		<main className="flex-1 grid place-content-center p-4">
			<section className="container  space-y-2 max-w-5xl text-center">
				<h1 className="text-3xl font-bold tracking-tighter sm:text-5xl xl:text-6xl">
					Build Better Habits, Track Your Progress
				</h1>
				<p className="px-14 text-center text-pretty text-muted-foreground md:text-xl">
					HabitTracker helps you build positive habits, break bad ones, and
					track your progress with beautiful visualizations.
				</p>
				<Button size="lg" className="mt-4 w-40 py-4" asChild>
					<Link to="/sign-up">
						Get started <ArrowRightIcon className="size-6" />
					</Link>
				</Button>
			</section>
		</main>
	);
}
