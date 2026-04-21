export interface User {
  id: string;
  name: string;
  email: string;
  password?: string;
  role: 'admin' | 'user';
  createdAt: Date;
}

export interface AuthResponse {
  token: string;
  user: User;
}
