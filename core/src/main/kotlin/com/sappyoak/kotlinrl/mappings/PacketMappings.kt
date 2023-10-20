package com.sappyoak.kotlinrl.mappings

import com.sappyoak.shared.RSClass
import com.sappyoak.shared.RSField
import com.sappyoak.shared.RSMethod

object PacketMappings {
    lateinit var packetWriterClass: RSClass
    lateinit var packetWriterField: RSClass

    lateinit var serverPacketClass: RSClass
    lateinit var serverPacketField: RSField
    lateinit var serverPacketLengthField: RSField

    lateinit var clientPacketClass: RSClass

    lateinit var packetBufferClass: RSClass
    lateinit var packetBufferField: RSField
    lateinit var packetBufferSizeField: RSField
    lateinit var addNodeMethod: RSMethod

    lateinit var packetBufferNodeField: RSField

    lateinit var isaacCipherClass: RSClass
    lateinit var isaacCipherField: RSField

    fun load() {
        packetWriterClass = ClientMappings.getRSClass("PacketWriter")!!
    }
}

/**
 * Client {
 *  val packetWriter: PacketWriter // ic
 *
 * PacketBuffer {
 *  val isaacCipher: IsaacCipher
 *  val bitIndex: Int
 *
 *  fun importIndex(Int)
 *  fun exportIndex(Byte)
 *  fun readByteIsaac(Byte): Int
 *  fun writeByteIsaac(Int, Int)
 *  fun readSmartByteShortIsaac(Byte)
 *  fun bitsRemaining(Int, Int) Int
 *  fun readBits(Int, Int) Int
 *  fun newIsaacCipher(IntArray, Int)
 *  fun setIsaacCipher(IsaacCipher, Int)
 * }
 *
 * PacketBufferNode {
 *   // static of BufferNode is an inner class
 *   val packetBufferNodes: Array<PacketBufferNode>
 *   val packetBufferNodeCount: Int
 *   val clientPacketLength: Int
 *
 *   fun release(Int)
 * }
 *
 * PacketWriter {
 *  val serverPacket: ServerPacket
 *  val packetBuffer: PacketBuffer
 *  val bufferSize: Int
 *  val packetBufferNodes: IterableNodeDeque
 *  val pendingWrites: Int
 *  val serverPacketLength: Int
 *
 *  fun addNode(PacketBufferNode, Byte(22))
 *  fun clearBuffer(Int)
 * }
 *
 * ClientPacket {
 *  val length: Int // dd
 *
 *  val NO_TIMEOUT // be
 *  val RESUME_PAUSEBUTTON // ba
 *  val DETECT_MODIFIED_CLIENT // ds
 *  val AFFINEDCLANSETTINGS_SETMUTED_FROMCHANNEL // (field = f10, obf = am)
 *  val OPOBJ2_ // (field = f11, obf = "av")
 * }
 *
 * ClientPacketMapping {
 *  val f1 = (f1, "al')
 *  val
 *  val f10 = (AFFINEDCLANSETTINGS_SETMUTED_FROMCHANNEL, "am")
 *  val f11 = (OPOBJ2_, "av")
 *  val f16 = (CLICKWORLDMAP, "aj")
 *  val f17 = (IF_BUTTON2, "an")
 *  val f18 = (f18, "au")
 * }
 *
 *
 * ServerPacketMapping {
 *   val alreadyMapped = [
 *      (CAM_LOOKAT_EASED_COORD, "el")
 *   ]
 * }
 */