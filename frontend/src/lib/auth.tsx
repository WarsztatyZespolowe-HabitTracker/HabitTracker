import {
	createContext,
	useCallback,
	useContext,
	useState,
	type PropsWithChildren,
} from "react";

export interface Auth {
	isAuthenticated: boolean;
	token: string | null;
	login: (token: string) => void;
	logout: () => void;
}

const AuthContext = createContext<Auth | null>(null);

const key = "auth-token";

function getStorageToken() {
	return localStorage.getItem(key);
}

function setStorageToken(token: string) {
	localStorage.setItem(key, token);
}

function removeStorageToken() {
	localStorage.removeItem(key);
}

export function AuthProvider({ children }: PropsWithChildren) {
	const [token, setToken] = useState<string | null>(getStorageToken());

	const isAuthenticated = !!token;

	const login = useCallback((token: string) => {
		setStorageToken(token);
		setToken(token);
	}, []);

	const logout = useCallback(() => {
		removeStorageToken();
		setToken(null);
	}, []);

	return (
		<AuthContext value={{ isAuthenticated, login, logout, token }}>
			{children}
		</AuthContext>
	);
}

export function useAuth() {
	const context = useContext(AuthContext);

	if (!context) {
		throw new Error("useAuth must be used within an AuthProvider");
	}

	return context;
}
