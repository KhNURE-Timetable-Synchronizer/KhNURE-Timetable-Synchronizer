import { queryOptions, useQuery } from "@tanstack/react-query"
import { useAuth } from "../utils/AuthProvider"
import { fetchAndHandleData } from "../utils/fetchData"

export const requestStatuses = [
  "CREATED",
  "ON PROCESSING",
  "PROCESSED",
  "DECLINED",
] as const
export type RequestStatus = (typeof requestStatuses)[number]
export type Request = {
  id: number
  email: string
  status: RequestStatus
  telegramAccount: string
  requestedTimetable: {
    id: number
    name: string
    khnureTimetableId: string
  }
}
export type RequestsResponse = {
  totalRequestsNumber: number
  totalPageNumber: number
  currentPageNumber: number
  requests: Request[]
}

export const requestsQueryKey = ["requests"]
const fetchRequests = async ({
  page,
  pageSize,
  onUnauthorized,
}: {
  page: number
  pageSize: number
  onUnauthorized: () => void
}) => {
  return fetchAndHandleData<RequestsResponse>(
    "GET Requests",
    onUnauthorized,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/request?page=${page}&pageSize=${pageSize}`
  )
}

export const requestQueryOptions = (requestId: number, logout: () => void) =>
  queryOptions({
    queryKey: ["request", requestId],
    queryFn: () =>
      fetchAndHandleData<Request>(
        "GET Request",
        logout,
        `${import.meta.env.VITE_BACKEND_URL}/api/v1/request/${requestId}`
      ),
  })

export const updateRequestStatus = async (
  requestId: number,
  status: RequestStatus,
  logout: () => void
) => {
  return fetchAndHandleData<Request>(
    "PUT Request",
    logout,
    `${import.meta.env.VITE_BACKEND_URL}/api/v1/request/${requestId}/status`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ status }),
    }
  )
}

export default function useRequestsAdmin({
  page,
  requestsPerPage,
}: {
  page: number
  requestsPerPage: number
}) {
  const { logout } = useAuth()
  const requests = useQuery({
    queryKey: [...requestsQueryKey, page, requestsPerPage],
    queryFn: () =>
      fetchRequests({
        page,
        pageSize: requestsPerPage,
        onUnauthorized: logout,
      }),
  })

  return { requests }
}
