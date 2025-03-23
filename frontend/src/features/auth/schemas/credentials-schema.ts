import { z } from "zod";

export const credentialsSchema = z.object({
	username: z.string().nonempty("Username is required"),
	password: z.string().min(8, "Password must be at least 8 characters"),
});

export type AuthCredentials = z.infer<typeof credentialsSchema>;
