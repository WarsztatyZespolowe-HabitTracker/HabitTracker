import { Header } from "@/components/home/header";
import { Hero } from "@/components/home/hero";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
	component: HomePage,
});

function HomePage() {
	return (
		<div className="flex min-h-screen flex-col">
			<Header />
			<Hero />
		</div>
	);
}
