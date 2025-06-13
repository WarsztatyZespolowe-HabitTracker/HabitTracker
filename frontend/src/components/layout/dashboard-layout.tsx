// dashboard-layout.tsx

import { Button } from "@/components/ui/button";
import { Logo } from "@/components/ui/logo";
import { SignOutButton } from "@/features/auth/components/sign-up-button";
import { Link } from "@tanstack/react-router";
import { useIsAdmin } from '@/features/auth/hooks/useIsAdmin';

import type { PropsWithChildren } from "react";

export function DashboardLayout({ children }: PropsWithChildren) {
    const { isAdmin } = useIsAdmin();
    return (
        <div className="flex flex-col min-h-screen">
            <header className="py-2 px-6 gap-6 flex justify-between items-center border-b border-border shadow-xs">
                <Logo asLink to="/habits" />
                <nav className="space-x-4">
                    {isAdmin && (
                        <Button variant="ghost" asChild>
                            <Link
                                to="/users"
                                activeProps={{
                                    className:
                                        "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                                }}
                            >
                                Admin Panel
                            </Link>
                        </Button>
                    )}
                    <Button variant="ghost" asChild>
                        <Link
                            to="/editUserdata"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                        >
                            Edit data
                        </Link>
                    </Button>
                    <SignOutButton variant="outline">Sign out</SignOutButton>
                </nav>
            </header>

            <nav className="bg-gray-100 py-2 px-6 border-b border-border">
                <div className="flex space-x-4">
                    <Button variant="ghost" asChild>
                        <Link
                            to="/habits"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                        >
                            Habits
                        </Link>
                    </Button>

                    <Button variant="ghost" asChild aria-label="Remind">
                        <Link
                            to="/remind"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                            className="flex items-center"
                        >
                            Remind
                        </Link>
                    </Button>

                    <Button variant="ghost" asChild>
                        <Link
                            to="/manage"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                        >
                            Manage
                        </Link>
                    </Button>
                    <Button variant="ghost" asChild>
                        <Link
                            to="/stats"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                        >
                            Stats
                        </Link>
                    </Button>
                    <Button variant="ghost" asChild>
                        <Link
                            to="/export"
                            activeProps={{
                                className:
                                    "bg-primary hover:bg-primary/90 text-primary-foreground hover:text-primary-foreground",
                            }}
                        >
                            Export
                        </Link>
                    </Button>
                </div>
            </nav>

            <main className="flex-1 py-6 px-3 sm:py-10 sm:px-6 md:py-12 md:px-8 container mx-auto space-y-4">
                {children}
            </main>
        </div>
    );
}
