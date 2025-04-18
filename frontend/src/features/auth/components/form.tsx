import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  credentialsSchema,
  type AuthCredentials,
} from "@/features/auth/schemas/credentials-schema";
import { Alert, AlertDescription } from "@/components/ui/alert";

interface AuthFormProps {
  submitText: string;
  onSubmit: (data: AuthCredentials) => void;
  errorMessage: string;
  isError?: boolean;
  isPending?: boolean;
}

export function AuthForm({
  onSubmit,
  submitText,
  errorMessage,
  isError,
  isPending,
}: AuthFormProps) {
  const form = useForm({
    resolver: zodResolver(credentialsSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  const isLoading = isPending || form.formState.isSubmitting;

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex flex-col gap-4 w-full"
      >
        <FormField
          control={form.control}
          name="username"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Username</FormLabel>
              <FormControl>
                <Input placeholder="Your username" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Password</FormLabel>
              <FormControl>
                <Input type="password" placeholder="********" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        {isError && (
          <Alert variant="destructive">
            <AlertDescription>{errorMessage}</AlertDescription>
          </Alert>
        )}
        <Button isLoading={isLoading} type="submit">
          {submitText}
        </Button>
      </form>
    </Form>
  );
}
