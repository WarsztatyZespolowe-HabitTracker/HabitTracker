import React from "react";
import { createFileRoute } from "@tanstack/react-router";
import { useAuth } from "@/lib/auth";

export const Route = createFileRoute("/_dashboard/export")({
    component: ExportPage,
});

export function ExportPage() {
    const { token } = useAuth();

    const handleExport = async () => {
        if (!token) {
            alert("You must be logged in to export habits.");
            return;
        }

        try {
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

            const habits = await res.json();

            // Tworzymy plik JSON do pobrania
            const blob = new Blob([JSON.stringify(habits, null, 2)], { type: "application/json" });
            const url = URL.createObjectURL(blob);

            const a = document.createElement("a");
            a.href = url;
            a.download = "habits-export.json";
            a.click();
            URL.revokeObjectURL(url);
        } catch (err) {
            console.error("Export error:", err);
            alert("Failed to export habits.");
        }
    };

    return (
        <div className="p-6 max-w-lg mx-auto">
            <h1 className="text-2xl font-semibold mb-4">Export Habits</h1>
            <button
                onClick={handleExport}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
                Export Habits as JSON
            </button>
        </div>
    );
}
