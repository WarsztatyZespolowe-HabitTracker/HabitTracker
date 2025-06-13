import {createFileRoute} from "@tanstack/react-router";
import {useEffect, useState} from "react";
import {Button} from "@/components/ui/button";
import {
    PencilIcon,
    Trash2Icon,
    EyeIcon,
    EyeOffIcon,
    RotateCcwIcon,
    BellIcon,
} from "lucide-react";
import Modal from "@/components/ui/modal";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/lib/auth";

export const Route = createFileRoute("/_dashboard/manage")({
    component: ManageHabitsPage,
});

type Habit = {
    id: string;
    name: string;
    description: string;
    repeat: string[];
    category: string;
    hidden: boolean;
    reminder: boolean;
};

const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];

function ManageHabitsPage() {
    const [habits, setHabits] = useState<Habit[]>([]);
    const [editingHabit, setEditingHabit] = useState<Habit | null>(null);
    const [addingNew, setAddingNew] = useState(false);
    const [confirmAction, setConfirmAction] = useState<{
        id: string;
        type: "reset" | "delete" | null;
    } | null>(null);

    const { token } = useAuth();

    const tokenObj = JSON.parse(token);
    const tokenString = `${tokenObj.username}:${tokenObj.password}`;
    const encodedAuth = btoa(tokenString);

    // GET HABITS
    useEffect(() => {
        if (!token) return;

        async function fetchHabits() {
            try {
                const tokenObj = JSON.parse(token);
                const tokenString = `${tokenObj.username}:${tokenObj.password}`;
                const encodedAuth = btoa(tokenString);

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
                console.log("habits data:\n", data);

                setHabits(data.map((h: any) => ({
                    id: h.id,
                    name: h.name,
                    description: h.description,
                    repeat: h.daysOfWeek,
                    category: h.category,
                    hidden: false,
                    reminder: h.reminder ?? true,
                })));
            } catch (error) {
                console.error("Error fetching habits:", error);
            }
        }

        fetchHabits();
    }, [token]);

    // FORM STATE
    const [form, setForm] = useState({
        name: "",
        description: "",
        repeat: [] as string[],
        category: "",
    });

    // OTWÓRZ FORMULARZ DO EDYCJI
    function openEditForm(habit: Habit) {
        setForm({
            name: habit.name,
            description: habit.description,
            repeat: habit.repeat,
            category: habit.category,
        });
        setEditingHabit(habit);
        setAddingNew(false);
        setConfirmAction(null);
    }

    // OTWÓRZ FORMULARZ DODAWANIA
    function openAddForm() {
        setForm({name: "", description: "", repeat: [], category: ""});
        setEditingHabit(null);
        setAddingNew(true);
        setConfirmAction(null);
    }

    // ZAMKNIJ FORMULARZ
    function closeForm() {
        setEditingHabit(null);
        setAddingNew(false);
        setConfirmAction(null);
    }

    // TOGGLE DNIA POWTARZANIA
    function toggleDay(day: string) {
        setForm((f) => {
            if (f.repeat.includes(day)) {
                return {...f, repeat: f.repeat.filter((d) => d !== day)};
            } else {
                return {...f, repeat: [...f.repeat, day]};
            }
        });
    }

    // ZAPISZ DANE (dodaj lub edytuj)
    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();

        if (!token) {
            alert("You are not authenticated.");
            return;
        }

        if (addingNew) {
            try {
                const res = await fetch("http://localhost:8090/api/habits", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Basic ${encodedAuth}`,
                    },
                    body: JSON.stringify({
                        name: form.name,
                        description: form.description,
                        category: form.category,
                        daysOfWeek: form.repeat,
                    }),
                });

                if (!res.ok) {
                    throw new Error("Failed to create habit");
                }

                const createdHabit = await res.json();

                setHabits((prev) => [
                    ...prev,
                    {
                        id: createdHabit.id,
                        name: createdHabit.name,
                        description: createdHabit.description,
                        repeat: createdHabit.daysOfWeek,
                        category: createdHabit.category,
                        hidden: false,
                        reminder: createdHabit ?? false,
                    },
                ]);

            } catch (error) {
                console.error("Error creating habit:", error);
                alert("There was an error creating the habit.");
            }
        } else if (editingHabit) {
            // Edycja habitów – do uzupełnienia w przyszłości
        }

        closeForm();
    }


    // TOGGLE UKRYCIA NAWYKU
    function toggleHide(id: string) {
        setHabits((prev) =>
            prev.map((h) =>
                h.id === id ? {...h, hidden: !h.hidden} : h
            )
        );

        // TU WYMIEŃ NA KOMUNIKACJĘ Z BACKEND: PATCH /api/habits/:id/hide
        // fetch(`/api/habits/${id}/hide`, { method: 'PATCH' })
    }

    // TOGGLE REMINDER
    function toggleReminder(id: string) {
        setHabits((prev) =>
            prev.map((h) =>
                h.id === id ? { ...h, reminder: !h.reminder } : h
            )
        );

        // Możesz tu dodać fetch do backendu np.
        // fetch(`/api/habits/${id}/reminder`, { method: 'PATCH', body: JSON.stringify({ reminder: newValue })})
    }

    // ROZPOCZNIJ POTWIERDZENIE RESET/DELETE
    function startConfirm(id: string, type: "reset" | "delete") {
        setConfirmAction({id, type});
    }

    // ANULUJ POTWIERDZENIE
    function cancelConfirm() {
        setConfirmAction(null);
    }

    // WYKONAJ RESET - tu mock: np. resetuje "ukrycie" i inne dane na domyślne
    function handleReset(id: string) {
        // Mock reset - tylko przykład
        setHabits((prev) =>
            prev.map((h) =>
                h.id === id ? {...h, hidden: false} : h
            )
        );

        setConfirmAction(null);

        // TU WYMIEŃ NA KOMUNIKACJĘ Z BACKEND: POST /api/habits/:id/reset
        // fetch(`/api/habits/${id}/reset`, { method: 'POST' })
    }

    // WYKONAJ DELETE
    async function handleDelete(id: string) {
        if (!token) return;

        try {
            const res = await fetch(`http://localhost:8090/api/habits/${id}`, {
                method: "DELETE",
                headers: {
                    "Authorization": `Basic ${encodedAuth}`,
                },
            });

            if (!res.ok) {
                throw new Error("Failed to delete habit");
            }

            // Usunięcie lokalnie po sukcesie
            setHabits((prev) => prev.filter((h) => h.id !== id));
        } catch (error) {
            console.error("Error deleting habit:", error);
            alert("Failed to delete habit.");
        } finally {
            setConfirmAction(null);
        }
    }


    return (
        <>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-semibold">Manage Habits</h1>
                {!addingNew && !editingHabit && (
                    <Button variant="success" onClick={openAddForm}>
                        Add New Habit
                    </Button>
                )}
            </div>


            {/* LISTA NAWYKÓW */}
            <div className="space-y-4 mb-6">
                {habits.map((habit) => {
                    const isConfirming =
                        confirmAction && confirmAction.id === habit.id;

                    return (
                        <div
                            key={habit.id}
                            className={`border rounded p-4 flex justify-between items-center ${
                                habit.hidden ? "bg-gray-100" : ""
                            }`}
                        >
                            <div>
                                <h2 className="text-lg font-semibold">
                                    {habit.name}{" "}
                                    {habit.hidden && (
                                        <span className="text-sm text-gray-500">(Hidden)</span>
                                    )}
                                </h2>
                                <p className="text-muted-foreground">{habit.description}</p>
                                <p className="text-sm">
                                    Repeat: {habit.repeat.join(", ")} | Category: {habit.category}
                                </p>
                            </div>

                            <div className="flex gap-2">
                                {isConfirming ? (
                                    <>
                                        <Button
                                            variant={
                                                confirmAction!.type === "reset"
                                                    ? "destructive"
                                                    : "destructive"
                                            }
                                            onClick={() =>
                                                confirmAction!.type === "reset"
                                                    ? handleReset(habit.id)
                                                    : handleDelete(habit.id)
                                            }
                                        >
                                            Confirm {confirmAction.type}
                                        </Button>
                                        <Button variant="outline" onClick={cancelConfirm}>
                                            Cancel
                                        </Button>
                                    </>
                                ) : (
                                    <>
                                        <Button variant="default" onClick={() => openEditForm(habit)}>
                                            <PencilIcon className="mr-1.5"/>
                                            Edit
                                        </Button>
                                        <Button variant="warning" onClick={() => startConfirm(habit.id, "reset")}>
                                            <RotateCcwIcon className="mr-1.5"/>
                                            Reset
                                        </Button>
                                        <Button
                                            variant={habit.reminder ? "yellow" : "yellowDark"}
                                            onClick={() => toggleReminder(habit.id)}
                                            className="flex items-center gap-1"
                                        >
                                            <BellIcon className="mr-1" />
                                            {habit.reminder ? "Remind On" : "Remind Off"}
                                        </Button>

                                        <Button
                                            variant={habit.hidden ? "secondary" : "outline"}
                                            onClick={() => toggleHide(habit.id)}
                                        >
                                            {habit.hidden ? (
                                                <>
                                                    <EyeIcon className="mr-1.5"/>
                                                    Unhide
                                                </>
                                            ) : (
                                                <>
                                                    <EyeOffIcon className="mr-1.5"/>
                                                    Hide
                                                </>
                                            )}
                                        </Button>
                                        <Button variant="destructive" onClick={() => startConfirm(habit.id, "delete")}>
                                            <Trash2Icon className="mr-1.5"/>
                                            Delete
                                        </Button>

                                    </>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* FORMULARZ DODAWANIA/EDYCJI */}
            <Modal isOpen={addingNew || editingHabit !== null} onClose={closeForm}>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <h2 className="text-xl font-semibold">
                        {addingNew ? "Add New Habit" : "Edit Habit"}
                    </h2>

                    <div>
                        <label className="block mb-1 font-medium" htmlFor="name">Name</label>
                        <Input
                            id="name"
                            type="text"
                            required
                            value={form.name}
                            onChange={(e) => setForm((f) => ({...f, name: e.target.value}))}
                            className="input input-bordered w-full"
                        />
                    </div>

                    <div>
                        <label className="block mb-1 font-medium" htmlFor="description">Description</label>
                        <Input
                            id="description"
                            type="text"
                            required
                            value={form.description}
                            onChange={(e) => setForm((f) => ({...f, description: e.target.value}))}
                            className="input input-bordered w-full"
                        />
                    </div>

                    <div>
                        <label className="block mb-1 font-medium">Repeat (Days of Week)</label>
                        <div className="flex gap-2 flex-wrap">
                            {daysOfWeek.map((day) => (
                                <button
                                    key={day}
                                    type="button"
                                    onClick={() => toggleDay(day)}
                                    className={`px-3 py-1 rounded border ${
                                        form.repeat.includes(day)
                                            ? "bg-primary text-white"
                                            : "bg-background"
                                    }`}
                                >
                                    {day}
                                </button>
                            ))}
                        </div>
                    </div>

                    <div>
                        <label className="block mb-1 font-medium" htmlFor="category">Category</label>
                        <Input
                            id="category"
                            type="text"
                            required
                            value={form.category}
                            onChange={(e) => setForm((f) => ({...f, category: e.target.value}))}
                            className="input input-bordered w-full"
                        />
                    </div>

                    <div className="flex gap-4">
                        <Button type="submit" variant="success">
                            {addingNew ? "Add Habit" : "Save Changes"}
                        </Button>
                        <Button type="button" variant="outline" onClick={closeForm}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Modal>
        </>
    );
}
