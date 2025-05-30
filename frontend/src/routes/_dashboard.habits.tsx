import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { CheckIcon, XIcon } from "lucide-react";

export const Route = createFileRoute("/_dashboard/habits")({
    component: HabitsPage,
});

type Habit = {
    id: string;
    name: string;
    description: string;
    status?: "completed" | "skipped"; // Status nawyku na dziś
};

// --- MOCKOWANE DANE ---
// W prawdziwej aplikacji dane będą pobierane z backendu,
// tutaj dla uproszczenia na początku jest mock.
const mockHabits: Habit[] = [
    {
        id: "1",
        name: "Exercise",
        description: "30 minutes of running",
    },
    {
        id: "2",
        name: "Read",
        description: "Read 20 pages of a book",
    },
];

function HabitsPage() {
    const [habits, setHabits] = useState<Habit[]>(mockHabits);

    // --- PRZYKŁAD KOMUNIKACJI Z BACKENDEM ---
    // Przykład funkcji, która po kliknięciu przycisku "Complete" lub "Skip" wyśle
    // odpowiedni request do backendu, a po sukcesie zaktualizuje stan na froncie.
    //
    // async function sendStatusToBackend(id: string, status: "completed" | "skipped") {
    //   try {
    //     const response = await fetch(`/api/habits/${id}/status`, {
    //       method: "PUT",
    //       headers: {
    //         "Content-Type": "application/json",
    //       },
    //       body: JSON.stringify({ status }),
    //     });
    //     if (!response.ok) throw new Error("Failed to update habit status");
    //     const updatedHabit = await response.json();
    //     // Aktualizacja stanu z odpowiedzią z backendu
    //     setHabits((prev) =>
    //       prev.map((habit) => (habit.id === id ? { ...habit, status: updatedHabit.status } : habit))
    //     );
    //   } catch (error) {
    //     console.error(error);
    //     alert("Nie udało się zaktualizować nawyku.");
    //   }
    // }

    function handleComplete(id: string) {
        // TU BĘDZIE WYWOLANIE sendStatusToBackend(id, "completed")
        // Dla mocka po prostu aktualizujemy lokalny stan:
        setHabits((prev) =>
            prev.map((habit) =>
                habit.id === id ? { ...habit, status: "completed" } : habit
            )
        );
    }

    function handleSkip(id: string) {
        // TU BĘDZIE WYWOLANIE sendStatusToBackend(id, "skipped")
        // Dla mocka po prostu aktualizujemy lokalny stan:
        setHabits((prev) =>
            prev.map((habit) =>
                habit.id === id ? { ...habit, status: "skipped" } : habit
            )
        );
    }

    return (
        <>
            <h1 className="text-3xl font-semibold mb-4">Your Habits for Today</h1>

            {habits.length === 0 && <p>No habits for today.</p>}

            <div className="space-y-4">
                {habits.map(({ id, name, description, status }) => (
                    <div
                        key={id}
                        className="border border-border rounded p-4 flex justify-between items-center"
                    >
                        <div>
                            <h2 className="font-semibold text-lg">{name}</h2>
                            <p className="text-muted-foreground">{description}</p>
                        </div>

                        <div>
                            {!status ? (
                                <>
                                    <Button variant="success" onClick={() => handleComplete(id)}>
                                        <CheckIcon className="mr-1.5" />
                                        Complete
                                    </Button>
                                    <Button variant="warning" className="ml-2" onClick={() => handleSkip(id)}>
                                        <XIcon className="mr-1.5" />
                                        Skip
                                    </Button>
                                </>
                            ) : (
                                <Button
                                    variant={status === "completed" ? "success" : "warning"}
                                    disabled
                                >
                                    {status === "completed" ? (
                                        <>
                                            <CheckIcon className="mr-1.5" />
                                            Completed
                                        </>
                                    ) : (
                                        <>
                                            <XIcon className="mr-1.5" />
                                            Skipped
                                        </>
                                    )}
                                </Button>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </>
    );
}
