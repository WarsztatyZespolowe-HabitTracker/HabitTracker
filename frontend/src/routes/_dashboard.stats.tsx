import {createFileRoute} from "@tanstack/react-router";
import React from "react";

export const Route = createFileRoute("/_dashboard/stats")({
    component: StatsPage,
});

type Habit = {
    id: string;
    name: string;
    description: string;
    category: string;
};

type HabitStreak = {
    habitId: string;
    streakCount: number; // liczba dni pod rząd
};

// --- MOCK DANYCH ---
// Na ten moment korzystamy z danych statycznych,
// ale poniżej znajdziesz wskazówki, jak podłączyć backend.
const habits: Habit[] = [
    {id: "1", name: "Exercise", description: "30 minutes running", category: "Health"},
    {id: "2", name: "Read", description: "Read 20 pages", category: "Education"},
    {id: "3", name: "Exercise", description: "30 minutes running", category: "Health"},
];

const habitStreaks: HabitStreak[] = [
    {habitId: "1", streakCount: 2},
    {habitId: "2", streakCount: 3},
    {habitId: "3", streakCount: 8},
];

function StatsPage() {
    // Poniżej możesz wprowadzić stan i efekt do pobierania danych z backendu
    // const [habits, setHabits] = React.useState<Habit[]>([]);
    // const [habitStreaks, setHabitStreaks] = React.useState<HabitStreak[]>([]);
    // const [loading, setLoading] = React.useState(true);
    // const [error, setError] = React.useState<string | null>(null);

    // React.useEffect(() => {
    //     async function fetchData() {
    //         try {
    //             setLoading(true);
    //             // 1. Pobierz nawyki z backendu
    //             const habitsResponse = await fetch('/api/habits');
    //             if (!habitsResponse.ok) throw new Error('Failed to fetch habits');
    //             const habitsData: Habit[] = await habitsResponse.json();
    //             setHabits(habitsData);

    //             // 2. Pobierz streaki z backendu
    //             const streaksResponse = await fetch('/api/habits/streaks');
    //             if (!streaksResponse.ok) throw new Error('Failed to fetch habit streaks');
    //             const streaksData: HabitStreak[] = await streaksResponse.json();
    //             setHabitStreaks(streaksData);
    //         } catch (err) {
    //             setError((err as Error).message);
    //         } finally {
    //             setLoading(false);
    //         }
    //     }
    //     fetchData();
    // }, []);

    // if (loading) return <p>Loading habits stats...</p>;
    // if (error) return <p className="text-red-500">Error: {error}</p>;

    return (
        <div className="max-w-3xl mx-auto p-6 relative">
            <h1 className="text-3xl font-semibold mb-6">Habits Stats</h1>
            <div className="space-y-4">
                {habits.map((habit) => {
                    const streak = habitStreaks.find((hs) => hs.habitId === habit.id)?.streakCount || 0;
                    const showCongrats = streak > 7;

                    return (
                        <div
                            key={habit.id}
                            className="border rounded p-4 flex flex-col justify-between items-center bg-white shadow-sm relative overflow-hidden"
                        >
                            {/* Jeśli streak > 7, pokazujemy wiadmość zachęcającą */}
                            {showCongrats}

                            <div className="w-full flex justify-between items-center mb-2">
                                <div>
                                    <h2 className="text-lg font-semibold">{habit.name}</h2>
                                    <p className="text-muted-foreground">{habit.description}</p>
                                    <p className="text-sm text-gray-500">Category: {habit.category}</p>
                                </div>
                                <div className="text-right">
                                    <p className="text-xl font-bold">{streak} days</p>
                                    <p className="text-sm text-gray-400">Streak</p>
                                </div>
                            </div>

                            {/* Wiadomość zachęcająca */}
                            {showCongrats && (
                                <div
                                    className="mt-2 w-full text-center text-green-700 font-semibold flex justify-center items-center gap-2 select-none">
                                    <span role="img" aria-label="party popper">🎉</span>
                                    Keep it up! You're on a roll!
                                    <span role="img" aria-label="party popper">🎉</span>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>

        </div>
    );
}
