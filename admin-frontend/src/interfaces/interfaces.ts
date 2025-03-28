interface IGarden {
    garden_id: string;
    locations: string;
    garden_status: string;
    acres: number;
    monthly_price: number;
}

interface IRental {
    rental_id: string;
    user_id: string;
    garden_id: string;
    rental_date: string;
}

interface IMarketItem {
    item_id: string;
    seller_id: string;
    product_info: {
        product_name: string;
        price_per_kg: number;
        quantity: number;
    };
    is_active: boolean;
}

interface ITicket {
    ticket_id: string;
    user_id: string;
    ticket_status: 'approved' | 'pending' | 'rejected';
    equipment: {
        equipment_name: string;
        equipment_quantity: number;
    }
    ticket_date: string;
    user_full_name: string;
    user_name: string;
}

interface IUser {
    user_id: string;
    username: string;
    full_name: {
        first_name: string;
        last_name: string;
    }
    user_role: 'admin' |  'tenant' | 'member';
}

export { IGarden, IRental, IMarketItem, ITicket, IUser };