import {createFileRoute} from "@tanstack/react-router";
import React, {useEffect, useState} from "react";
import {useAuth} from "@/lib/auth";

export const Route = createFileRoute("/_dashboard/stats")({
    component: StatsPage,
});

type Habit = {
    id: string;
    name: string;
    description: string;
    category: string;
    streak: number;
};

function StatsPage() {
    const [habits, setHabits] = useState<Habit[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const {token} = useAuth();

    useEffect(() => {
        async function fetchHabits() {
            if (!token) return;

            try {
                setLoading(true);
                const tokenObj = JSON.parse(token);
                const encodedAuth = btoa(`${tokenObj.username}:${tokenObj.password}`);

                const res = await fetch("http://localhost:8090/api/habits", {
                    method: "GET",
                    headers: {
                        "Authorization": `Basic ${encodedAuth}`,
                    },
                });

                if (!res.ok) {
                    throw new Error("Failed to fetch habits");
                }

                const data = await res.json();

                setHabits(
                    data.map((h: any) => ({
                        id: h.id,
                        name: h.name,
                        description: h.description,
                        category: h.category,
                        streak: h.streak ?? 0,
                    }))
                );
            } catch (err) {
                setError((err as Error).message);
            } finally {
                setLoading(false);
            }
        }

        fetchHabits();
    }, [token]);

    if (loading) return <p>Loading habits stats...</p>;
    if (error) return <p className="text-red-500">Error: {error}</p>;

    const streaks = habits.map(h => h.streak);
    const maxStreak = Math.max(...streaks, 0);
    const minStreak = Math.min(...streaks, 0);
    const avgStreak = streaks.length > 0 ? streaks.reduce((a, b) => a + b, 0) / streaks.length : 0;
    return (
        <div className="max-w-3xl mx-auto p-6 relative">
            <h1 className="text-3xl font-semibold mb-6">Habits Stats</h1>

            {/* --- Stats Summary Bar --- */}
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
                <div className="bg-green-100 rounded p-4 text-center shadow">
                    <p className="text-sm text-green-800 font-medium">Longest Streak</p>
                    <p className="text-2xl font-bold text-green-900">{maxStreak} days</p>
                </div>
                <div className="bg-yellow-100 rounded p-4 text-center shadow">
                    <p className="text-sm text-yellow-800 font-medium">Average Streak</p>
                    <p className="text-2xl font-bold text-yellow-900">{avgStreak.toFixed(1)} days</p>
                </div>
                <div className="bg-orange-100 rounded p-4 text-center shadow">
                    <p className="text-sm text-orange-800 font-medium">Shortest Streak</p>
                    <p className="text-2xl font-bold text-orange-900">{minStreak} days</p>
                </div>
            </div>

            {/* --- Habits List --- */}
            <div className="space-y-4">
                {habits.map((habit) => {
                    const showCongrats = habit.streak > 7;

                    return (
                        <div
                            key={habit.id}
                            className="border rounded p-4 flex flex-col justify-between items-center bg-white shadow-sm relative overflow-hidden"
                        >
                            <div className="w-full flex justify-between items-center mb-2">
                                <div>
                                    <h2 className="text-lg font-semibold">{habit.name}</h2>
                                    <p className="text-muted-foreground">{habit.description}</p>
                                    <p className="text-sm text-gray-500">Category: {habit.category}</p>
                                </div>
                                <div className="text-right">
                                    <p className="text-xl font-bold">{habit.streak} days</p>
                                    <p className="text-sm text-gray-400">Streak</p>
                                </div>
                            </div>

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
