export const formatPages = ({
  currentPage,
  totalPages = 1,
}: {
  currentPage: number
  totalPages?: number
}): { label: string | number; value?: number; key: string | number }[] => {
  let pages: {
    label: string | number
    value?: number
    key: string | number
  }[] = []

  if (totalPages <= 6) {
    pages = Array(totalPages)
      .fill(null)
      .map((_, index) => ({
        label: index + 1,
        value: index + 1,
        key: index + 1,
      }))
  } else if (currentPage <= 3) {
    pages = [
      ...Array.from({ length: 4 }, (_, i) => ({
        label: i + 1,
        value: i + 1,
        key: i + 1,
      })),
      { label: "...", value: undefined, key: "first..." },
      { label: totalPages, value: totalPages, key: totalPages },
    ]
  } else if (currentPage > 3 && currentPage < totalPages - 2) {
    pages = [
      { label: 1, value: 1, key: 1 },
      { label: "...", value: undefined, key: "first..." },
      { label: currentPage - 1, value: currentPage - 1, key: currentPage - 1 },
      { label: currentPage, value: currentPage, key: currentPage },
      { label: currentPage + 1, value: currentPage + 1, key: currentPage + 1 },
      { label: "...", value: undefined, key: "last..." },
      { label: totalPages, value: totalPages, key: totalPages },
    ]
  } else {
    pages = [
      { label: 1, value: 1, key: 1 },
      { label: "...", value: undefined, key: "first..." },
      ...Array.from({ length: 4 }, (_, i) => ({
        label: totalPages - 3 + i,
        value: totalPages - 3 + i,
        key: totalPages - 3 + i,
      })),
    ]
  }

  // Add "<" and ">" pages
  pages = [
    {
      label: "<",
      value: currentPage > 1 ? currentPage - 1 : undefined,
      key: "prev",
    },
    ...pages,
    {
      label: ">",
      value: currentPage < totalPages ? currentPage + 1 : undefined,
      key: "next",
    },
  ]

  return pages
}
