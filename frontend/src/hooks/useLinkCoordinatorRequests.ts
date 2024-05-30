import { useQuery } from "@tanstack/react-query"
import { useAuth } from "../utils/AuthProvider"
import { fetchAndHandleData } from "../utils/fetchData"

export type LinkCoordinatorRequest = {
  id: number
  name: string
  type: string
  requested: boolean
}

export const linkCoordinatorRequestsQueryKey = ["personalRequest"]
const fetchLinkCoordinatorRequests = async ({
  onUnauthorized,
}: {
  onUnauthorized: () => void
}) => {
  return fetchAndHandleData<LinkCoordinatorRequest[]>(
    "GET Link Coordinator Requests",
    onUnauthorized,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/request/timetable`
  )
}

export default function useLinkCoordinatorRequests() {
  const { logout } = useAuth()
  const requests = useQuery({
    queryKey: linkCoordinatorRequestsQueryKey,
    queryFn: () => fetchLinkCoordinatorRequests({ onUnauthorized: logout }),
  })

  const createRequest = async (params: {
    khnureTimetableId: string
    telegramAccount: string
  }) => {
    await fetchAndHandleData(
      "POST Link Coordinator Request",
      logout,
      `${import.meta.env.VITE_BACKEND_URL}/api/v1/request`,
      {
        method: "POST",
        body: JSON.stringify({ ...params }),
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      }
    ).catch(error => {
      console.error(error)
      alert("Failed to add request")
      throw error
    })
  }

  return { requests, createRequest }
}
