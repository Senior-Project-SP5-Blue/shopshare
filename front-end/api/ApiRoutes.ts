import Config from 'react-native-config';

class ApiRoutes {
  #baseURL: string = Config.API_URL ?? '';
  #url: string;

  constructor() {
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

export type apiPathParams = {
  groupId: string;
  memberId?: string;
  listId?: string;
  itemId?: string;
};

export default new ApiRoutes();
