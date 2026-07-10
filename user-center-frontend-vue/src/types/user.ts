export interface User {
  id?: number;
  username?: string;
  userAccount?: string;
  avatarUrl?: string;
  gender?: number;
  userRole?: number;
  phone?: string;
  email?: string;
  userStatus?: number;
  createTime?: string;
}

export interface ApiResponse<T> {
  code: string;
  message: string;
  data?: T;
}

export interface LoginParams {
  userAccount: string;
  userPassword: string;
}

export interface RegisterParams extends LoginParams {
  username?: string;
  checkPassword: string;
}

export interface ProfileUpdateParams {
  username?: string;
  avatarUrl?: string;
  gender?: number;
  phone?: string;
  email?: string;
}

export interface DeleteUserParams {
  id: number;
}
