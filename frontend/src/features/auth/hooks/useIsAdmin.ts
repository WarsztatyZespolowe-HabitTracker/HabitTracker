import { useAuth } from '@/lib/auth';

export function useIsAdmin() {
  const { token } = useAuth();
  
  if (!token) {
    return { isAdmin: false };
  }
  
  try {
    const userData = JSON.parse(token);
    return { isAdmin: userData.role === 'ADMIN' };
  } catch (error) {
    console.error('Error parsing token:', error);
    return { isAdmin: false };
  }
}