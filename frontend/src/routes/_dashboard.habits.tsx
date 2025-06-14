import { createFileRoute } from "@tanstack/react-router";
import {useEffect, useState} from "react";
import { Button } from "@/components/ui/button";
import { CheckIcon, XIcon } from "lucide-react";
import {useAuth} from "@/lib/auth.tsx";

export const Route = createFileRoute("/_dashboard/habits")({
    component: HabitsPage,
});

function HabitsPage() {
    const [habits, setHabits] = useState<HabitWithStatus[]>([]);
    const [showHidden, setShowHidden] = useState(false);
    const { token } = useAuth();

    const tokenObj = JSON.parse(token);
    const tokenString = `${tokenObj.username}:${tokenObj.password}`;
    const encodedAuth = btoa(tokenString);

    type HabitHistory = {
        date: string; // ISO string
        completed: boolean;
        skipped: boolean;
    };

    type HabitWithStatus = Habit & {
        status?: "completed" | "skipped";
    };

    type Habit = {
        id: string;
        name: string;
        description: string;
        repeat: string[];
        category: string;
        hidden: boolean;
        history: HabitHistory[];
    };

    useEffect(() => {
        if (!token) return;

        async function fetchHabits() {
            try {
                const res = await fetch("http://localhost:8090/api/habits/today", {
                    method: "GET",
                    headers: {
                        "Authorization": `Basic ${encodedAuth}`,
                    },
                });

                if (!res.ok) {
                    throw new Error("Failed to fetch habits");
                }

                const data = await res.json();

                const today = new Date().toISOString().split("T")[0]; // yyyy-mm-dd

                const processedHabits = data.map((h: any) => {
                    const todayEntry = h.history?.find((entry: any) => {
                        const entryDate = new Date(entry.date).toISOString().split("T")[0];
                        return entryDate === today;
                    });

                    let status: "completed" | "skipped" | undefined = undefined;
                    if (todayEntry) {
                        if (todayEntry.completed) status = "completed";
                        else if (todayEntry.skipped) status = "skipped";
                    }

                    return {
                        id: h.id,
                        name: h.name,
                        description: h.description,
                        repeat: h.daysOfWeek,
                        category: h.category,
                        hidden: h.hidden,
                        history: h.history,
                        status,
                    };
                });

                setHabits(processedHabits);
            } catch (error) {
                console.error("Error fetching habits:", error);
            }
        }

        fetchHabits();
    }, [token]);

    async function handleComplete(id: string) {
        try {
            const res = await fetch(`http://localhost:8090/api/habits/${id}/complete`, {
                method: "PUT",
                headers: {
                    "Authorization": `Basic ${encodedAuth}`,
                },
            });

            if (!res.ok) {
                throw new Error("Failed to mark habit as completed");
            }

            setHabits((prev) =>
                prev.map((habit) =>
                    habit.id === id ? { ...habit, status: "completed" } : habit
                )
            );
        } catch (error) {
            console.error("Error completing habit:", error);
        }
    }

    async function handleSkip(id: string) {
        try {
            const res = await fetch(`http://localhost:8090/api/habits/${id}/skip`, {
                method: "PUT",
                headers: {
                    "Authorization": `Basic ${encodedAuth}`,
                },
            });

            if (!res.ok) {
                throw new Error("Failed to mark habit as skipped");
            }

            setHabits((prev) =>
                prev.map((habit) =>
                    habit.id === id ? { ...habit, status: "skipped" } : habit
                )
            );
        } catch (error) {
            console.error("Error skipping habit:", error);
        }
    }

    return (
        <>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-3xl font-semibold">Your Habits for Today</h1>
                <Button
                    variant="outline"
                    onClick={() => setShowHidden((prev) => !prev)}
                >
                    {showHidden ? "Hide Hidden" : "Show Hidden"}
                </Button>
            </div>

            {habits.length === 0 && <p>No habits for today.</p>}

            <div className="space-y-4">
                {habits
                    .filter((habit) => showHidden || !habit.hidden)
                    .map((habit) => (
                        <div
                            key={habit.id}
                            className={`border border-border rounded p-4 flex justify-between items-center ${
                                habit.hidden ? "bg-gray-100" : ""
                            }`}
                        >
                        <div>
                            <h2 className="font-semibold text-lg">{habit.name}</h2>
                            <p className="text-muted-foreground">{habit.description}</p>
                        </div>

                        <div>
                            {!habit.status ? (
                                <>
                                    <Button variant="success" onClick={() => handleComplete(habit.id)}>
                                        <CheckIcon className="mr-1.5" />
                                        Complete
                                    </Button>
                                    <Button variant="warning" className="ml-2" onClick={() => handleSkip(habit.id)}>
                                        <XIcon className="mr-1.5" />
                                        Skip
                                    </Button>
                                </>
                            ) : (
                                <Button
                                    variant={habit.status === "completed" ? "success" : "warning"}
                                    disabled
                                >
                                    {habit.status === "completed" ? (
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

