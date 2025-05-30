import React from "react";
import {createFileRoute} from "@tanstack/react-router";

export const Route = createFileRoute("/_dashboard/export")({
    component: ExportPage,
});

const mockHabits = [
    { id: "1", name: "Exercise", description: "30 minutes running", category: "Health" },
    { id: "2", name: "Read", description: "Read 20 pages", category: "Education" },
];

export function ExportPage() {
    const handleExport = () => {
        // Tu zamiast mocka wywołaj API, które zwróci JSON z bazy
        const dataToExport = mockHabits;

        // Tworzymy blob i link do pobrania
        const blob = new Blob([JSON.stringify(dataToExport, null, 2)], { type: "application/json" });
        const url = URL.createObjectURL(blob);

        // Tworzymy tymczasowy link, klikamy i usuwamy
        const a = document.createElement("a");
        a.href = url;
        a.download = "habits-export.json";
        a.click();
        URL.revokeObjectURL(url);
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
