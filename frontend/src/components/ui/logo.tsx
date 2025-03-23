import { cn } from "@/lib/utils";
import { Link, type LinkProps } from "@tanstack/react-router";
import { CheckCheckIcon } from "lucide-react";

interface BasicLogoProps {
  className?: string;
}

interface StandardLogoProps extends BasicLogoProps {
  asLink?: never;
  to?: never;
}

interface LinkLogoProps extends BasicLogoProps {
  asLink: true;
  to: LinkProps["to"];
}

type LogoProps = StandardLogoProps | LinkLogoProps;

export function Logo({ asLink, to, className }: LogoProps) {
  const Comp = asLink ? Link : "div";

  return (
    <Comp
      to={to}
      aria-label={asLink ? "Go to homepage" : undefined}
      className={cn("flex gap-2 items-center text-xl font-bold", className)}
    >
      <CheckCheckIcon className="size-6 text-primary" />
      <span>HabitTracker</span>
    </Comp>
  );
}
