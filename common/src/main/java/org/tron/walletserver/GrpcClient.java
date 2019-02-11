package org.tron.walletserver;

import android.text.TextUtils;

import com.google.protobuf.ByteString;

import org.tron.api.GrpcAPI;
import org.tron.api.WalletExtensionGrpc;
import org.tron.api.WalletGrpc;
import org.tron.api.WalletSolidityGrpc;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
public class GrpcClient {

  private ManagedChannel channelFull = null;
  private ManagedChannel channelSolidity = null;
  private WalletGrpc.WalletBlockingStub blockingStubFull = null;
  private WalletSolidityGrpc.WalletSolidityBlockingStub blockingStubSolidity = null;
  private WalletExtensionGrpc.WalletExtensionBlockingStub blockingStubExtension = null;


  public GrpcClient(String fullnode, String soliditynode) {
    if (!TextUtils.isEmpty(fullnode)) {
      channelFull = ManagedChannelBuilder.forTarget(fullnode)
              .usePlaintext(true)
              .build();
      blockingStubFull = WalletGrpc.newBlockingStub(channelFull);
    }
    if (!TextUtils.isEmpty(soliditynode)) {
      channelSolidity = ManagedChannelBuilder.forTarget(soliditynode)
              .usePlaintext(true)
              .build();
      blockingStubSolidity = WalletSolidityGrpc.newBlockingStub(channelSolidity);
      blockingStubExtension = WalletExtensionGrpc.newBlockingStub(channelSolidity);
    }
  }

  public void shutdown() {
    if (channelFull != null) {
      channelFull.shutdown();
    }
    if (channelSolidity != null) {
      channelSolidity.shutdown();
    }
  }

  public Protocol.Account queryAccount(byte[] address, boolean useSolidity) {
      ByteString addressBS = ByteString.copyFrom(address);
      Protocol.Account request = Protocol.Account.newBuilder().setAddress(addressBS).build();
      if (blockingStubSolidity != null && useSolidity) {
        return blockingStubSolidity.getAccount(request);
      } else {
        return blockingStubFull.getAccount(request);
      }
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.AccountUpdateContract contract) {
    return blockingStubFull.updateAccount2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.UpdateAssetContract contract) {
    return blockingStubFull.updateAsset2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.TransferContract contract) {
    return blockingStubFull.createTransaction2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.FreezeBalanceContract contract) {
    return blockingStubFull.freezeBalance2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.WithdrawBalanceContract contract) {
    return blockingStubFull.withdrawBalance2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.UnfreezeBalanceContract contract) {
    return blockingStubFull.unfreezeBalance2(contract);
  }

  public GrpcAPI.TransactionExtention createTransaction(Contract.UnfreezeAssetContract contract) {
    return blockingStubFull.unfreezeAsset2(contract);
  }

  public GrpcAPI.TransactionExtention createTransferAssetTransaction(Contract.TransferAssetContract contract) {
    return blockingStubFull.transferAsset2(contract);
  }

  public GrpcAPI.TransactionExtention createParticipateAssetIssueTransaction(
          Contract.ParticipateAssetIssueContract contract) {
    return blockingStubFull.participateAssetIssue2(contract);
  }

  public GrpcAPI.TransactionExtention createAssetIssue(Contract.AssetIssueContract contract) {
    return blockingStubFull.createAssetIssue2(contract);
  }

  public GrpcAPI.TransactionExtention voteWitnessAccount(Contract.VoteWitnessContract contract) {
    return blockingStubFull.voteWitnessAccount2(contract);
  }

  public GrpcAPI.TransactionExtention createWitness(Contract.WitnessCreateContract contract) {
    return blockingStubFull.createWitness2(contract);
  }

  public GrpcAPI.TransactionExtention updateWitness(Contract.WitnessUpdateContract contract) {
    return blockingStubFull.updateWitness2(contract);
  }

  public boolean broadcastTransaction(Protocol.Transaction signaturedTransaction) {
    int i = 10;
    GrpcAPI.Return response = blockingStubFull.broadcastTransaction(signaturedTransaction);
    while (response.getResult() == false && response.getCode() == GrpcAPI.Return.response_code.SERVER_BUSY
            && i > 0) {
      i--;
      response = blockingStubFull.broadcastTransaction(signaturedTransaction);
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (response.getResult() == false) {
    }
    return response.getResult();
  }

  public GrpcAPI.BlockExtention getBlock(long blockNum, boolean useSolidity) {
    if (blockNum < 0) {
      if (blockingStubSolidity != null && useSolidity) {
        return blockingStubSolidity.getNowBlock2(GrpcAPI.EmptyMessage.newBuilder().build());
      } else {
        return blockingStubFull.getNowBlock2(GrpcAPI.EmptyMessage.newBuilder().build());
      }
    }
    GrpcAPI.NumberMessage.Builder builder = GrpcAPI.NumberMessage.newBuilder();
    builder.setNum(blockNum);
    if (blockingStubSolidity != null) {
      return blockingStubSolidity.getBlockByNum2(builder.build());
    } else {
      return blockingStubFull.getBlockByNum2(builder.build());
    }
  }

//  public Optional<AccountList> listAccounts() {
//    AccountList accountList = blockingStubSolidity
//        .listAccounts(EmptyMessage.newBuilder().build());
//    return Optional.ofNullable(accountList);
//
//  }

  public GrpcAPI.WitnessList listWitnesses(boolean useSolidity) {
    if (blockingStubSolidity != null && useSolidity) {
      GrpcAPI.WitnessList witnessList = blockingStubSolidity.listWitnesses(GrpcAPI.EmptyMessage.newBuilder().build());
      return (witnessList);
    } else {
      GrpcAPI.WitnessList witnessList = blockingStubFull.listWitnesses(GrpcAPI.EmptyMessage.newBuilder().build());
      return (witnessList);
    }
  }

  public GrpcAPI.AssetIssueList getAssetIssueList(boolean useSolidity) {
    if (blockingStubSolidity != null && useSolidity) {
      GrpcAPI.AssetIssueList assetIssueList = blockingStubSolidity
              .getAssetIssueList(GrpcAPI.EmptyMessage.newBuilder().build());
      return (assetIssueList);
    } else {
      GrpcAPI.AssetIssueList assetIssueList = blockingStubFull
              .getAssetIssueList(GrpcAPI.EmptyMessage.newBuilder().build());
      return (assetIssueList);
    }
  }

  public GrpcAPI.NodeList listNodes() {
    GrpcAPI.NodeList nodeList = blockingStubFull.listNodes(GrpcAPI.EmptyMessage.newBuilder().build());
    return (nodeList);
  }

  public GrpcAPI.AssetIssueList getAssetIssueByAccount(byte[] address) {
    ByteString addressBS = ByteString.copyFrom(address);
    Protocol.Account request = Protocol.Account.newBuilder().setAddress(addressBS).build();
    GrpcAPI.AssetIssueList assetIssueList = blockingStubFull.getAssetIssueByAccount(request);
    return (assetIssueList);
  }

  public GrpcAPI.AccountNetMessage getAccountNet(byte[] address) {
    ByteString addressBS = ByteString.copyFrom(address);
    Protocol.Account request = Protocol.Account.newBuilder().setAddress(addressBS).build();
    return blockingStubFull.getAccountNet(request);
  }

  public GrpcAPI.AccountResourceMessage getAccountRes(byte[] address) {
    ByteString addressBS = ByteString.copyFrom(address);
    Protocol.Account request = Protocol.Account.newBuilder().setAddress(addressBS).build();
    return blockingStubFull.getAccountResource(request);
  }

  public Contract.AssetIssueContract getAssetIssueByName(String assetName) {
    ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
    GrpcAPI.BytesMessage request = GrpcAPI.BytesMessage.newBuilder().setValue(assetNameBs).build();
    return blockingStubFull.getAssetIssueByName(request);
  }

  public GrpcAPI.NumberMessage getTotalTransaction() {
    return blockingStubFull.totalTransaction(GrpcAPI.EmptyMessage.newBuilder().build());
  }

  public GrpcAPI.NumberMessage getNextMaintenanceTime() {
    return blockingStubFull.getNextMaintenanceTime(GrpcAPI.EmptyMessage.newBuilder().build());
  }

  public GrpcAPI.TransactionListExtention getTransactionsFromThis(byte[] address, int offset, int limit) {
    ByteString addressBS = ByteString.copyFrom(address);
    Protocol.Account account = Protocol.Account.newBuilder().setAddress(addressBS).build();
    GrpcAPI.AccountPaginated.Builder accountPaginated = GrpcAPI.AccountPaginated.newBuilder();
    accountPaginated.setAccount(account);
    accountPaginated.setOffset(offset);
    accountPaginated.setLimit(limit);
    GrpcAPI.TransactionListExtention transactionList = blockingStubExtension
            .getTransactionsFromThis2(accountPaginated.build());
    return (transactionList);
  }

  public GrpcAPI.TransactionListExtention getTransactionsToThis(byte[] address, int offset, int limit) {
    ByteString addressBS = ByteString.copyFrom(address);
    Protocol.Account account = Protocol.Account.newBuilder().setAddress(addressBS).build();
    GrpcAPI.AccountPaginated.Builder accountPaginated = GrpcAPI.AccountPaginated.newBuilder();
    accountPaginated.setAccount(account);
    accountPaginated.setOffset(offset);
    accountPaginated.setLimit(limit);
    GrpcAPI.TransactionListExtention transactionList = blockingStubExtension
            .getTransactionsToThis2(accountPaginated.build());
    return (transactionList);
  }

  public Protocol.Transaction getTransactionById(String txID, boolean useSolidity) {
    ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(txID));
    GrpcAPI.BytesMessage request = GrpcAPI.BytesMessage.newBuilder().setValue(bsTxid).build();
    if (blockingStubSolidity != null && useSolidity) {
      Protocol.Transaction transaction = blockingStubSolidity.getTransactionById(request);
      return (transaction);
    } else {
      Protocol.Transaction transaction = blockingStubFull.getTransactionById(request);
      return (transaction);
    }
  }

  public Protocol.Block getBlockById(String blockID) {
    ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(blockID));
    GrpcAPI.BytesMessage request = GrpcAPI.BytesMessage.newBuilder().setValue(bsTxid).build();
    Protocol.Block block = blockingStubFull.getBlockById(request);
    return (block);
  }

  public GrpcAPI.BlockListExtention getBlockByLimitNext(long start, long end) {
    GrpcAPI.BlockLimit.Builder builder = GrpcAPI.BlockLimit.newBuilder();
    builder.setStartNum(start);
    builder.setEndNum(end);
    GrpcAPI.BlockListExtention blockList = blockingStubFull.getBlockByLimitNext2(builder.build());
    return (blockList);
  }

  public GrpcAPI.BlockListExtention getBlockByLatestNum(long num) {
    GrpcAPI.NumberMessage numberMessage = GrpcAPI.NumberMessage.newBuilder().setNum(num).build();
    GrpcAPI.BlockListExtention blockList = blockingStubFull.getBlockByLatestNum2(numberMessage);
    return (blockList);
  }

  public Protocol.TransactionInfo getTransactionInfo(String txID) {
    ByteString bsTxid = ByteString.copyFrom(ByteArray.fromHexString(txID));
    GrpcAPI.BytesMessage request = GrpcAPI.BytesMessage.newBuilder().setValue(bsTxid).build();
    if (blockingStubSolidity != null) {
      return blockingStubSolidity.getTransactionInfoById(request);
    }
    return Protocol.TransactionInfo.getDefaultInstance();
  }
}