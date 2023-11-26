type ShopperGroupDto = {
  id: string;
  name: string;
  admin: string;
  users: string[];
  lists: SlimList[];
};

type SlimList = {
  id: string;
  name: string;
};

export default ShopperGroupDto;
