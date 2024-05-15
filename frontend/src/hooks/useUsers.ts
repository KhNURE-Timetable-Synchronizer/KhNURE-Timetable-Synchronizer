import { useQuery } from "@tanstack/react-query"

export type User = {
  id: number
  email: string
  role: "USER" | "ADMIN"
}
export type UsersResponse = {
  totalUsersNumber: number
  totalPageNumber: number
  currentPageNumber: number
  users: User[]
}

export const usersQueryKey = ["users"]
const fetchUsers = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/v1/users`, {
    credentials: "include",
  })
  if (!res.ok) {
    throw new Error("GET Users response was not ok")
  }
  const data = await res.json()
  return data as UsersResponse
}

export default function useUsers() {
  const users = useQuery({
    queryKey: usersQueryKey,
    queryFn: fetchUsers,
  })

  return { users }
}
