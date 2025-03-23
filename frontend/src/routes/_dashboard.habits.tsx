import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_dashboard/habits")({
  component: HabitsPage,
});

function HabitsPage() {
  return (
    <>
      <h1 className="text-3xl font-semibold">Your Habits</h1>
      <div className="border border-border rounded-sm p-4">Habits table</div>
    </>
  );
}
