/*
 * Copyright (C) 2016 - 2017 Aurum
 *
 * Mystery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mystery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aurum.mystery2.game;

import com.aurum.mystery2.BitConverter;
import com.aurum.mystery2.ByteBuffer;
import com.aurum.mystery2.ByteOrder;

public class Item implements Cloneable {
    // Entry fields
    public String name, description;
    public int namePointer, descriptionPointer;
    public long buyPrice, sellPrice;
    public short icon, palette, type, subtype, move, order;
    public boolean throwingDamage;
    
    // Unknown fields
    public short unkThrowing1B, unkThrowing1C;
    public boolean unkFood1, unkFood2;
    
    // Static fields
    public static final int SIZE = 0x20;
    
    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    public static Item unpack(ByteBuffer buffer) {
        Item item = new Item();
        
        int nextOffset = buffer.position() + SIZE;
        
        // Fields
        item.namePointer = buffer.readInt();
        item.buyPrice = buffer.readUInt();
        item.sellPrice = buffer.readUInt();
        item.type = buffer.readUByte();
        item.icon = buffer.readUByte();
        buffer.skip(0x2);
        item.descriptionPointer = buffer.readInt();
        item.unkFood1 = buffer.readBoolean();
        item.unkFood2 = buffer.readBoolean();
        item.throwingDamage = buffer.readBoolean();
        buffer.skip(0x1);
        item.move = buffer.readShort();
        item.order = buffer.readUByte();
        item.unkThrowing1B = buffer.readUByte();
        item.unkThrowing1C = buffer.readUByte();
        item.palette = buffer.readUByte();
        item.subtype = buffer.readUByte();
        buffer.skip(0x1);
        
        // Strings
        buffer.seek(BitConverter.pointerToOffset(item.namePointer));
        item.name = buffer.readString();
        buffer.seek(BitConverter.pointerToOffset(item.descriptionPointer));
        item.description = buffer.readString();
        
        buffer.seek(nextOffset);
        
        return item;
    }
    
    public static byte[] pack(Item item) {
        ByteBuffer buffer = new ByteBuffer(0x20, ByteOrder.LITTLE_ENDIAN);
        
        buffer.writeInt(item.namePointer);
        buffer.writeUInt(item.buyPrice);
        buffer.writeUInt(item.sellPrice);
        buffer.writeUByte(item.type);
        buffer.writeUByte(item.icon);
        buffer.writeShort((short) 0);
        buffer.writeInt(item.descriptionPointer);
        buffer.writeBoolean(item.unkFood1);
        buffer.writeBoolean(item.unkFood2);
        buffer.writeBoolean(item.throwingDamage);
        buffer.writeByte((byte) 0);
        buffer.writeShort(item.move);
        buffer.writeUByte(item.order);
        buffer.writeUByte(item.unkThrowing1B);
        buffer.writeUByte(item.unkThrowing1C);
        buffer.writeUByte(item.palette);
        buffer.writeUByte(item.subtype);
        buffer.writeByte((byte) 0);
        
        return buffer.getContent();
    }
}