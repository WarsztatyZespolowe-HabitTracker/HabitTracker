import { useState } from "react";
import {createFileRoute} from "@tanstack/react-router";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export const Route = createFileRoute("/_dashboard/editUserdata")({
  component: EditUserDataPage,
});

export default function EditUserDataPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [newUsername, setNewUsername] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleUpdate = async () => {
    const basicAuth = btoa(`${username}:${password}`);

    try {
      const res = await fetch(`http://localhost:8090/api/edituser/${username}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Basic ${basicAuth}`,
        },
        body: JSON.stringify({
          username: newUsername,
          password: newPassword,
        }),
      });

      if (!res.ok) {
        throw new Error(`HTTP ${res.status}`);
      }

      // Sprawdź, czy odpowiedź ma treść
      let data = null;
      const contentType = res.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        data = await res.json();
      }

      setMessage(`Zaktualizowano użytkownika: ${data?.username ?? newUsername}`);
    } catch (err) {
      setMessage("Błąd aktualizacji. Sprawdź dane logowania.");
      console.error(err);
    }
  };


  return (
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <Card className="w-full max-w-md shadow-lg border-0">
          <CardHeader>
            <CardTitle className="text-2xl font-bold text-center">Edytuj dane</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Obecna nazwa użytkownika</label>
              <Input
                  type="text"
                  placeholder="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="mt-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Obecne hasło</label>
              <Input
                  type="password"
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="mt-1"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Nowa nazwa użytkownika</label>
              <Input
                  type="text"
                  placeholder="New Username"
                  value={newUsername}
                  onChange={(e) => setNewUsername(e.target.value)}
                  className="mt-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Nowe hasło</label>
              <Input
                  type="password"
                  placeholder="New Password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="mt-1"
              />
            </div>

            <Button className="w-full" onClick={handleUpdate}>
              Zapisz zmiany
            </Button>
            {message && <p>{message}</p>}
          </CardContent>
        </Card>
      </div>
  );
}
