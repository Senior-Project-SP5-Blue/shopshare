class ApiRoutes {
  #baseURL: string;
  #url: string;

  constructor(baseURL: string) {
    this.#baseURL = baseURL;
    this.#url = '';
  }

  users(userId?: string) {
    this.#url = `${this.#baseURL}/users${userId ?? ''}`;
    return this;
  }

  groups(groupId?: string) {
    this.#url += `/groups${groupId ?? ''}`;
    return this;
  }

  members(memberId?: string) {
    this.#url += `/members${memberId ?? ''}`;
    return this;
  }

  invitations(groupId?: string) {
    this.#url += `/invitations${groupId ?? ''}`;
    return this;
  }

  lists(listId?: string) {
    this.#url += `/lists${listId ?? ''}`;
    return this;
  }

  items(itemId?: string) {
    this.#url += `/items${itemId ?? ''}`;
    return this;
  }

  buildUrl() {
    return this.#url;
  }
}

export default new ApiRoutes('http://localhost:8080');
