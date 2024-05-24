import { useQuery } from "@tanstack/react-query"
import { useAuth } from "../utils/AuthProvider"
import { fetchAndHandleData } from "../utils/fetchData"

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
const fetchUsers = async ({
  page,
  pageSize,
  onUnauthorized,
}: {
  page: number
  pageSize: number
  onUnauthorized: () => void
}) => {
  return fetchAndHandleData<UsersResponse>(
    "GET Users",
    onUnauthorized,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/users?page=${page}&pageSize=${pageSize}`
  )
}

export default function useUsers({
  page,
  usersPerPage,
}: {
  page: number
  usersPerPage: number
}) {
  const { logout } = useAuth()
  const users = useQuery({
    queryKey: [...usersQueryKey, page, usersPerPage],
    queryFn: () =>
      fetchUsers({ page, pageSize: usersPerPage, onUnauthorized: logout }),
  })

  return { users }
}
