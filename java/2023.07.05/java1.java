package com.soiiy.beta.trade.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soiiy.alpha.bootstrap.config.BootstrapWebRequest;
import com.soiiy.alpha.bootstrap.error.RuntimeError;
import com.soiiy.alpha.bootstrap.http.ResultEntity;
import com.soiiy.alpha.bootstrap.http.ResultPage;
import com.soiiy.alpha.bootstrap.pojo.Convertible;
import com.soiiy.alpha.bootstrap.pojo.ConvertibleKt;
import com.soiiy.alpha.security.SecurityThrowable;
import com.soiiy.alpha.security.provide.authorize.AuthorizeEntity;
import com.soiiy.alpha.security.servlet.ServletAuthorizeKit;
import com.soiiy.beta.trade.data.dto.OrderBuildRaw;
import com.soiiy.beta.trade.data.dto.OrderCreateDTO;
import com.soiiy.beta.trade.data.entity.OrderDetail;
import com.soiiy.beta.trade.data.entity.OrderEntity;
import com.soiiy.beta.trade.data.model.OrderReasonModel;
import com.soiiy.beta.trade.data.pojo.OrderPrepayDTO;
import com.soiiy.beta.trade.data.pojo.OrderQueryDTO;
import com.soiiy.beta.trade.data.result.OrderCountResult;
import com.soiiy.beta.trade.data.result.OrderCreateResult;
import com.soiiy.beta.trade.data.result.OrderDetailResult;
import com.soiiy.beta.trade.data.result.OrderPrepareResult;
import com.soiiy.beta.trade.data.result.SubjectDetailResult;
import com.soiiy.beta.trade.data.vo.PaymentCombineDTO;
import com.soiiy.beta.trade.data.vo.PaymentDetailDTO;
import com.soiiy.beta.trade.data.vo.PaymentPrepayDTO;
import com.soiiy.beta.trade.data.vo.ProductBuildDTO;
import com.soiiy.beta.trade.data.vo.ProductCancelDTO;
import com.soiiy.beta.trade.data.vo.ProductPaymentDTO;
import com.soiiy.beta.trade.data.vo.ProductResultExtra;
import com.soiiy.beta.trade.data.vo.ProductResultItem;
import com.soiiy.beta.trade.data.vo.ProductResultParam;
import com.soiiy.beta.trade.data.vo.ProductResultRaw;
import com.soiiy.beta.trade.data.vo.ProductResultStatus;
import com.soiiy.beta.trade.repos.OrderQueryRepo;
import com.soiiy.beta.trade.repos.OrderRepository;
import com.soiiy.beta.trade.repos.ReportRepository;
import com.soiiy.beta.trade.service.feigns.ServiceFaceFeign;
import com.soiiy.beta.trade.service.feigns.ServicePayFeign;
import com.soiiy.beta.trade.service.feigns.ServiceProdFeign;
import com.soiiy.beta.trade.support.errors.OrderException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Metadata(
   mv = {1, 8, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000¬\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J$\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u0002002\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020\u000102H\u0017J\u0018\u00103\u001a\u0002042\u0006\u0010/\u001a\u0002002\u0006\u00105\u001a\u000206H\u0017J\u0010\u00107\u001a\u0002002\u0006\u0010/\u001a\u000200H\u0016J\u0010\u00108\u001a\u00020.2\u0006\u0010/\u001a\u000200H\u0017J\b\u00109\u001a\u00020:H\u0016J\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0017J\u0010\u0010?\u001a\u0002042\u0006\u0010/\u001a\u000200H\u0016J\u0010\u0010@\u001a\u00020A2\u0006\u0010/\u001a\u000200H\u0016J\u0010\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020EH\u0016J\u0018\u0010F\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010G\u001a\u000200H\u0012J$\u0010F\u001a\u0002042\u0006\u0010/\u001a\u0002002\u0012\u0010H\u001a\u000e\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020002H\u0017J\u0010\u0010I\u001a\u00020J2\u0006\u0010=\u001a\u00020>H\u0016J\u0012\u0010K\u001a\u0004\u0018\u00010\u00012\u0006\u0010=\u001a\u00020LH\u0017J$\u0010M\u001a\u00020A2\u0006\u0010/\u001a\u0002002\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020\u000102H\u0017J\u0016\u0010N\u001a\b\u0012\u0004\u0012\u00020A0O2\u0006\u0010=\u001a\u00020PH\u0016J\u0010\u0010Q\u001a\u0002042\u0006\u0010/\u001a\u000200H\u0017J\u0014\u0010R\u001a\u000e\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020S02H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0015\u001a\u00020\u00168\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001e\u0010\u001b\u001a\u00020\u001c8\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001e\u0010!\u001a\u00020\"8\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001e\u0010'\u001a\u00020(8\u0016@\u0016X\u0097.¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,¨\u0006T"},
   d2 = {"Lcom/soiiy/beta/trade/service/OrderService;", "", "()V", "faces", "Lcom/soiiy/beta/trade/service/feigns/ServiceFaceFeign;", "getFaces", "()Lcom/soiiy/beta/trade/service/feigns/ServiceFaceFeign;", "setFaces", "(Lcom/soiiy/beta/trade/service/feigns/ServiceFaceFeign;)V", "payment", "Lcom/soiiy/beta/trade/service/feigns/ServicePayFeign;", "getPayment", "()Lcom/soiiy/beta/trade/service/feigns/ServicePayFeign;", "setPayment", "(Lcom/soiiy/beta/trade/service/feigns/ServicePayFeign;)V", "prods", "Lcom/soiiy/beta/trade/service/feigns/ServiceProdFeign;", "getProds", "()Lcom/soiiy/beta/trade/service/feigns/ServiceProdFeign;", "setProds", "(Lcom/soiiy/beta/trade/service/feigns/ServiceProdFeign;)V", "quiries", "Lcom/soiiy/beta/trade/repos/OrderQueryRepo;", "getQuiries", "()Lcom/soiiy/beta/trade/repos/OrderQueryRepo;", "setQuiries", "(Lcom/soiiy/beta/trade/repos/OrderQueryRepo;)V", "reports", "Lcom/soiiy/beta/trade/repos/ReportRepository;", "getReports", "()Lcom/soiiy/beta/trade/repos/ReportRepository;", "setReports", "(Lcom/soiiy/beta/trade/repos/ReportRepository;)V", "repository", "Lcom/soiiy/beta/trade/repos/OrderRepository;", "getRepository", "()Lcom/soiiy/beta/trade/repos/OrderRepository;", "setRepository", "(Lcom/soiiy/beta/trade/repos/OrderRepository;)V", "request", "Lcom/soiiy/alpha/bootstrap/config/BootstrapWebRequest;", "getRequest", "()Lcom/soiiy/alpha/bootstrap/config/BootstrapWebRequest;", "setRequest", "(Lcom/soiiy/alpha/bootstrap/config/BootstrapWebRequest;)V", "async", "Lcom/soiiy/alpha/bootstrap/http/ResultEntity;", "tradeNo", "", "body", "", "cancel", "", "reason", "Lcom/soiiy/beta/trade/data/model/OrderReasonModel;", "comment", "confirm", "count", "Lcom/soiiy/beta/trade/data/result/OrderCountResult;", "create", "Lcom/soiiy/beta/trade/data/result/OrderCreateResult;", "dto", "Lcom/soiiy/beta/trade/data/dto/OrderCreateDTO;", "delete", "detail", "Lcom/soiiy/beta/trade/data/result/OrderDetailResult;", "onCancel", "", "entity", "Lcom/soiiy/beta/trade/data/entity/OrderEntity;", "onPayment", "tradeOut", "content", "prepare", "Lcom/soiiy/beta/trade/data/result/OrderPrepareResult;", "prepay", "Lcom/soiiy/beta/trade/data/pojo/OrderPrepayDTO;", "provide", "query", "Lcom/soiiy/alpha/bootstrap/http/ResultPage;", "Lcom/soiiy/beta/trade/data/pojo/OrderQueryDTO;", "receipt", "total", "", "service-trade-project"}
)
@SourceDebugExtension({"SMAP\nOrderService.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OrderService.kt\ncom/soiiy/beta/trade/service/OrderService\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Convertible.kt\ncom/soiiy/alpha/bootstrap/pojo/ConvertibleKt\n+ 5 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 6 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n*L\n1#1,411:1\n766#2:412\n857#2,2:413\n766#2:416\n857#2,2:417\n1477#2:420\n1502#2,3:421\n1505#2,3:431\n766#2:437\n857#2,2:438\n1549#2:440\n1620#2,2:441\n1622#2:444\n1549#2:445\n1620#2,2:446\n1622#2:449\n1855#2,2:450\n1549#2:453\n1620#2,3:454\n1549#2:457\n1620#2,3:458\n1549#2:461\n1620#2,3:462\n1#3:415\n21#4:419\n21#4:443\n21#4:448\n21#4:465\n361#5,7:424\n125#6:434\n152#6,2:435\n154#6:452\n*S KotlinDebug\n*F\n+ 1 OrderService.kt\ncom/soiiy/beta/trade/service/OrderService\n*L\n81#1:412\n81#1:413,2\n84#1:416\n84#1:417,2\n135#1:420\n135#1:421,3\n135#1:431,3\n154#1:437\n154#1:438,2\n166#1:440\n166#1:441,2\n166#1:444\n167#1:445\n167#1:446,2\n167#1:449\n168#1:450,2\n224#1:453\n224#1:454,3\n285#1:457\n285#1:458,3\n345#1:461\n345#1:462,3\n103#1:419\n166#1:443\n167#1:448\n393#1:465\n135#1:424,7\n136#1:434\n136#1:435,2\n136#1:452\n*E\n"})
public class OrderService {
   @Autowired
   public OrderRepository repository;
   @Autowired
   public ReportRepository reports;
   @Autowired
   public OrderQueryRepo quiries;
   @Autowired
   public ServiceFaceFeign faces;
   @Autowired
   public ServiceProdFeign prods;
   @Autowired
   public ServicePayFeign payment;
   @Autowired
   public BootstrapWebRequest request;

   @NotNull
   public OrderRepository getRepository() {
      OrderRepository var10000 = this.repository;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("repository");
         return null;
      }
   }

   public void setRepository(@NotNull OrderRepository <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.repository = <set-?>;
   }

   @NotNull
   public ReportRepository getReports() {
      ReportRepository var10000 = this.reports;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("reports");
         return null;
      }
   }

   public void setReports(@NotNull ReportRepository <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.reports = <set-?>;
   }

   @NotNull
   public OrderQueryRepo getQuiries() {
      OrderQueryRepo var10000 = this.quiries;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("quiries");
         return null;
      }
   }

   public void setQuiries(@NotNull OrderQueryRepo <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.quiries = <set-?>;
   }

   @NotNull
   public ServiceFaceFeign getFaces() {
      ServiceFaceFeign var10000 = this.faces;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("faces");
         return null;
      }
   }

   public void setFaces(@NotNull ServiceFaceFeign <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.faces = <set-?>;
   }

   @NotNull
   public ServiceProdFeign getProds() {
      ServiceProdFeign var10000 = this.prods;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("prods");
         return null;
      }
   }

   public void setProds(@NotNull ServiceProdFeign <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.prods = <set-?>;
   }

   @NotNull
   public ServicePayFeign getPayment() {
      ServicePayFeign var10000 = this.payment;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("payment");
         return null;
      }
   }

   public void setPayment(@NotNull ServicePayFeign <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.payment = <set-?>;
   }

   @NotNull
   public BootstrapWebRequest getRequest() {
      BootstrapWebRequest var10000 = this.request;
      if (var10000 != null) {
         return var10000;
      } else {
         Intrinsics.throwUninitializedPropertyAccessException("request");
         return null;
      }
   }

   public void setRequest(@NotNull BootstrapWebRequest <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.request = <set-?>;
   }

   @NotNull
   public Map total() {
      QueryWrapper query = new QueryWrapper();
      String[] var2 = new String[]{"status", "COUNT(*) AS total"};
      query.select(var2);
      query.gt("total_fee", 0);
      query.groupBy("status");
      List results = this.getRepository().getEntities().getMapper().selectMaps((Wrapper)query);
      Intrinsics.checkNotNullExpressionValue(results, "results");
      Iterable $this$filter$iv = (Iterable)results;
      int $i$f$filter = false;
      Collection destination$iv$iv = (Collection)(new ArrayList());
      int $i$f$filterTo = false;
      Iterator var9 = $this$filter$iv.iterator();

      while(var9.hasNext()) {
         Object element$iv$iv = var9.next();
         Map it = (Map)element$iv$iv;
         int var12 = false;
         if (Intrinsics.areEqual(it.get("status"), "WAIT_CONFIRM")) {
            destination$iv$iv.add(element$iv$iv);
         }
      }

      $this$filter$iv = (Iterable)((List)destination$iv$iv);
      int var17 = 0;

      int var15;
      boolean $i$f$filterTo;
      for(Iterator var6 = $this$filter$iv.iterator(); var6.hasNext(); var17 += var15) {
         Object var22 = var6.next();
         Map it = (Map)var22;
         $i$f$filterTo = false;
         var15 = (new BigDecimal(String.valueOf(it.get("total")))).intValue();
      }

      int confirm = var17;
      Iterable $this$filter$iv = (Iterable)results;
      int $i$f$filter = false;
      Collection destination$iv$iv = (Collection)(new ArrayList());
      $i$f$filterTo = false;
      Iterator var29 = $this$filter$iv.iterator();

      while(var29.hasNext()) {
         Object element$iv$iv = var29.next();
         Map it = (Map)element$iv$iv;
         int var13 = false;
         if (Intrinsics.areEqual(it.get("status"), "WAIT_PROVIDE")) {
            destination$iv$iv.add(element$iv$iv);
         }
      }

      $this$filter$iv = (Iterable)((List)destination$iv$iv);
      int var20 = 0;

      for(Iterator var23 = $this$filter$iv.iterator(); var23.hasNext(); var20 += var15) {
         Object var26 = var23.next();
         Map it = (Map)var26;
         int var30 = false;
         var15 = (new BigDecimal(String.valueOf(it.get("total")))).intValue();
      }

      Pair[] var21 = new Pair[]{TuplesKt.to("order_confirm", confirm), TuplesKt.to("order_provide", var20)};
      return MapsKt.mapOf(var21);
   }

   @NotNull
   public OrderCountResult count() {
      return this.getQuiries().count();
   }

   @NotNull
   public ResultPage query(@NotNull OrderQueryDTO dto) {
      Intrinsics.checkNotNullParameter(dto, "dto");
      return this.getQuiries().query(dto);
   }

   @NotNull
   public OrderDetailResult detail(@NotNull String tradeNo) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      return this.getQuiries().detail(tradeNo);
   }

   @NotNull
   public OrderPrepareResult prepare(@NotNull OrderCreateDTO dto) {
      Intrinsics.checkNotNullParameter(dto, "dto");
      ServiceProdFeign var10000 = this.getProds();
      Convertible $this$convert$iv = (Convertible)dto;
      int $i$f$convert = false;
      ProductResultRaw prepared = var10000.build((ProductBuildDTO)ConvertibleKt.convert($this$convert$iv, Reflection.getOrCreateKotlinClass(ProductBuildDTO.class)));
      OrderPrepareResult result = new OrderPrepareResult();
      Iterable var14 = (Iterable)prepared.getExtras();
      int var5 = 0;

      Iterator var6;
      Object var7;
      boolean var9;
      int var12;
      for(var6 = var14.iterator(); var6.hasNext(); var5 += var12) {
         var7 = var6.next();
         ProductResultExtra it = (ProductResultExtra)var7;
         var9 = false;
         var12 = it.getAmount();
      }

      result.setTotalExtra(var5);
      var14 = (Iterable)prepared.getItems();
      var5 = 0;

      for(var6 = var14.iterator(); var6.hasNext(); var5 += var12) {
         var7 = var6.next();
         ProductResultItem it = (ProductResultItem)var7;
         var9 = false;
         var12 = it.getAmount();
      }

      result.setTotalItem(var5);
      result.setTotalFee(result.getTotalExtra() + result.getTotalItem());
      result.setExtras(prepared.getExtras());
      result.setItems(prepared.getItems());
      return result;
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   @NotNull
   public OrderCreateResult create(@NotNull OrderCreateDTO dto) {
      Intrinsics.checkNotNullParameter(dto, "dto");
      AuthorizeEntity principal = ServletAuthorizeKit.INSTANCE.entity(true);
      if (!Intrinsics.areEqual(principal.getGuard(), "bind")) {
         throw SecurityThrowable.Companion.getFORBIDDEN();
      } else {
         String tradeUser = principal.key();
         Map var5 = principal.getProps();
         String tradeUserRaw = var5 != null ? (String)var5.get("binding.union") : null;
         CharSequence var42 = (CharSequence)tradeUserRaw;
         if (var42 == null || StringsKt.isBlank(var42)) {
            throw SecurityThrowable.Companion.getFORBIDDEN();
         } else {
            ProductResultRaw raw;
            String tradeUnion;
            Object var48;
            label109: {
               tradeUnion = OrderRepository.tradeNo$default(this.getRepository(), "s", 0, 2, (Object)null);
               ProductBuildDTO params = new ProductBuildDTO();
               params.setItems(dto.getItems());
               params.setOption(dto.getOption());
               params.setTradeUnion(tradeUnion);
               raw = this.getProds().build(params);
               Map var10000 = ((ProductResultItem)CollectionsKt.first(raw.getItems())).getDetails();
               if (var10000 != null) {
                  var48 = var10000.get("item_type");
                  if (var48 != null) {
                     break label109;
                  }
               }

               var48 = "";
            }

            Object type = var48;
            String[] var10 = new String[]{"svc_prod", "prod_" + type};
            List tags = CollectionsKt.listOf(var10);
            Iterable $this$groupBy$iv = (Iterable)raw.getItems();
            int $i$f$groupBy = false;
            Map destination$iv$iv = (Map)(new LinkedHashMap());
            int $i$f$groupByTo = false;
            Iterator var16 = $this$groupBy$iv.iterator();

            while(var16.hasNext()) {
               Object element$iv$iv = var16.next();
               ProductResultItem it = (ProductResultItem)element$iv$iv;
               int var19 = false;
               Object key$iv$iv = it.getSubject();
               int $i$f$getOrPut = false;
               Object value$iv$iv$iv = destination$iv$iv.get(key$iv$iv);
               if (value$iv$iv$iv == null) {
                  int var24 = false;
                  Object answer$iv$iv$iv = (List)(new ArrayList());
                  destination$iv$iv.put(key$iv$iv, answer$iv$iv$iv);
                  var48 = answer$iv$iv$iv;
               } else {
                  var48 = value$iv$iv$iv;
               }

               List list$iv$iv = (List)var48;
               list$iv$iv.add(element$iv$iv);
            }

            int $i$f$map = false;
            Collection destination$iv$iv = (Collection)(new ArrayList(destination$iv$iv.size()));
            int $i$f$mapTo = false;
            Iterator var50 = destination$iv$iv.entrySet().iterator();

            while(var50.hasNext()) {
               Map.Entry item$iv$iv = (Map.Entry)var50.next();
               int var56 = false;
               String key = (String)item$iv$iv.getKey();
               List details = (List)item$iv$iv.getValue();
               ServiceFaceFeign var52 = this.getFaces();
               String var10001 = key;
               if (key == null) {
                  var10001 = "";
               }

               SubjectDetailResult subject = var52.subject(var10001);
               String tradeNo = OrderRepository.tradeNo$default(this.getRepository(), "1", 0, 2, (Object)null);
               OrderEntity order = new OrderEntity();
               order.setType("item");
               order.setTags(tags);
               order.setTradeNo(tradeNo);
               order.setTradeUnion(tradeUnion);
               order.setTitle(subject.getBasic().getName());
               order.setThumbUrl(subject.getBasic().getLogo());
               order.setSubject(subject.getBasic().getId());
               order.setTradeUser(tradeUser);
               order.setTradeUserRaw(tradeUserRaw);
               Map var51 = dto.getOption();
               if (var51 == null) {
                  var51 = MapsKt.emptyMap();
               }

               order.setOption(var51);
               order.setKeywords(raw.getKeywords());
               order.setExpiredAt(LocalDateTime.now().plusMinutes(15L));
               Iterable $this$forEach$iv = (Iterable)raw.getExtras();
               int $i$f$forEach = false;
               Collection destination$iv$iv = (Collection)(new ArrayList());
               int $i$f$mapTo = false;
               Iterator var31 = $this$forEach$iv.iterator();

               Object item$iv$iv;
               ProductResultExtra it;
               boolean var34;
               while(var31.hasNext()) {
                  item$iv$iv = var31.next();
                  it = (ProductResultExtra)item$iv$iv;
                  var34 = false;
                  if (Intrinsics.areEqual(key, it.getSubject())) {
                     destination$iv$iv.add(item$iv$iv);
                  }
               }

               List extras = (List)destination$iv$iv;
               $this$forEach$iv = (Iterable)details;
               int var61 = 0;

               Iterator var28;
               int var38;
               Object element$iv;
               boolean var65;
               for(var28 = $this$forEach$iv.iterator(); var28.hasNext(); var61 += var38) {
                  element$iv = var28.next();
                  ProductResultItem s = (ProductResultItem)element$iv;
                  var65 = false;
                  var38 = s.getAmount();
               }

               order.setTotalItem(var61);
               $this$forEach$iv = (Iterable)extras;
               var61 = 0;

               for(var28 = $this$forEach$iv.iterator(); var28.hasNext(); var61 += var38) {
                  element$iv = var28.next();
                  ProductResultExtra s = (ProductResultExtra)element$iv;
                  var65 = false;
                  var38 = s.getAmount();
               }

               order.setTotalExtra(var61);
               order.setTotalFee(order.getTotalItem() + order.getTotalExtra());
               order.setContent(CollectionsKt.joinToString$default((Iterable)details, (CharSequence)",", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null.INSTANCE, 30, (Object)null));
               this.getRepository().entity(order);
               this.getReports().order(order);
               $this$forEach$iv = (Iterable)details;
               $i$f$forEach = false;
               destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($this$forEach$iv, 10)));
               $i$f$mapTo = false;
               var31 = $this$forEach$iv.iterator();

               Convertible $this$convert$iv;
               boolean $i$f$convert;
               while(var31.hasNext()) {
                  item$iv$iv = var31.next();
                  ProductResultItem it = (ProductResultItem)item$iv$iv;
                  var34 = false;
                  $this$convert$iv = (Convertible)it;
                  $i$f$convert = false;
                  destination$iv$iv.add((OrderDetail)ConvertibleKt.convert($this$convert$iv, Reflection.getOrCreateKotlinClass(OrderDetail.class)));
               }

               Collection var55 = (Collection)CollectionsKt.toMutableList((Collection)((List)destination$iv$iv));
               $this$forEach$iv = (Iterable)extras;
               Collection var36 = var55;
               $i$f$forEach = false;
               destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($this$forEach$iv, 10)));
               $i$f$mapTo = false;
               var31 = $this$forEach$iv.iterator();

               while(var31.hasNext()) {
                  item$iv$iv = var31.next();
                  it = (ProductResultExtra)item$iv$iv;
                  var34 = false;
                  $this$convert$iv = (Convertible)it;
                  $i$f$convert = false;
                  destination$iv$iv.add((OrderDetail)ConvertibleKt.convert($this$convert$iv, Reflection.getOrCreateKotlinClass(OrderDetail.class)));
               }

               $this$forEach$iv = (Iterable)CollectionsKt.plus(var36, (Iterable)((List)destination$iv$iv));
               $i$f$forEach = false;
               var28 = $this$forEach$iv.iterator();

               while(var28.hasNext()) {
                  element$iv = var28.next();
                  OrderDetail row = (OrderDetail)element$iv;
                  var65 = false;
                  row.setOid(order.getId());
                  row.setAmount(row.getAmount() - row.getReduce());
                  this.getRepository().detail(row);
               }

               destination$iv$iv.add(order);
            }

            List orders = (List)destination$iv$iv;
            OrderEntity entity = this.getRepository().multiple(orders);
            OrderCreateResult result = new OrderCreateResult();
            result.setTradeNo(entity.getTradeNo());
            result.setTotalFee(entity.getTotalFee());
            result.setTotalItem(entity.getTotalItem());
            result.setTotalExtra(entity.getTotalExtra());
            result.setTotalReduce(entity.getTotalReduce());
            return result;
         }
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   public boolean cancel(@NotNull String tradeNo, @NotNull OrderReasonModel reason) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      Intrinsics.checkNotNullParameter(reason, "reason");
      OrderEntity var10000 = this.getRepository().entity(tradeNo);
      if (var10000 == null) {
         return false;
      } else {
         OrderEntity entity = var10000;
         if (!Intrinsics.areEqual(entity.getStatus(), "WAIT_PAY")) {
            throw new RuntimeError("当前订单无法取消", 0, 2, (DefaultConstructorMarker)null);
         } else {
            entity.getSubject();
            entity.getTradeUnion();
            entity.setReasonInfo(reason);
            this.onCancel(entity);
            Unit it = Unit.INSTANCE;
            int var5 = false;
            return true;
         }
      }
   }

   public boolean delete(@NotNull String tradeNo) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      OrderEntity var10000 = this.getRepository().entity(tradeNo);
      if (var10000 == null) {
         return false;
      } else {
         OrderEntity entity = var10000;
         String principal = ServletAuthorizeKit.INSTANCE.key(true);
         if (!Intrinsics.areEqual(entity.getTradeUser(), principal)) {
            throw new RuntimeError("非法请求�?", 0, 2, (DefaultConstructorMarker)null);
         } else {
            return this.getRepository().delete(entity);
         }
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   @Nullable
   public Object prepay(@NotNull OrderPrepayDTO dto) {
      Intrinsics.checkNotNullParameter(dto, "dto");
      OrderEntity var10000 = this.getRepository().entity(dto.getTradeNo());
      if (var10000 == null) {
         throw new OrderException("订单不存在！", (Object)null, 2, (DefaultConstructorMarker)null);
      } else {
         OrderEntity entity = var10000;
         if (!Intrinsics.areEqual(entity.getStatus(), "WAIT_PAY")) {
            throw new OrderException("请勿重复支付，刷新页面后再试�?", (Object)null, 2, (DefaultConstructorMarker)null);
         } else {
            String notifyUrl = this.getRequest().serve("/orders/" + dto.getTradeNo() + "/on-payment");
            if (!Intrinsics.areEqual(entity.getType(), "multiple")) {
               PaymentPrepayDTO params = new PaymentPrepayDTO();
               params.setType(dto.getChannel());
               params.setTitle(entity.getContent());
               params.setTotalFee(entity.getTotalFee());
               params.setTradeNo(entity.getTradeNo());
               params.setThumbUrl(entity.getThumbUrl());
               params.setSubject(entity.getSubject());
               params.setNotifyUrl(notifyUrl);
               return this.getPayment().prepay(params);
            } else {
               List orders = this.getRepository().entities(dto.getTradeNo());
               PaymentCombineDTO combine = new PaymentCombineDTO();
               combine.setType(dto.getChannel());
               combine.setSerialNo(entity.getTradeNo());
               combine.setNotifyUrl(notifyUrl);
               Iterable $this$map$iv = (Iterable)orders;
               int $i$f$map = false;
               Collection destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10)));
               int $i$f$mapTo = false;
               Iterator var11 = $this$map$iv.iterator();

               while(var11.hasNext()) {
                  Object item$iv$iv = var11.next();
                  OrderEntity it = (OrderEntity)item$iv$iv;
                  int var14 = false;
                  PaymentDetailDTO detail = new PaymentDetailDTO();
                  detail.setTitle(it.getContent());
                  detail.setTotalFee(it.getTotalFee());
                  detail.setTradeNo(it.getTradeNo());
                  detail.setThumbUrl(it.getThumbUrl());
                  detail.setSubject(it.getSubject());
                  destination$iv$iv.add(detail);
               }

               combine.setOrders((List)destination$iv$iv);
               return this.getPayment().combine(combine);
            }
         }
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   public boolean onPayment(@NotNull String tradeNo, @NotNull Map content) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      Intrinsics.checkNotNullParameter(content, "content");
      OrderEntity var10000 = this.getRepository().entity(tradeNo);
      if (var10000 == null) {
         return false;
      } else {
         OrderEntity entity = var10000;
         String[] var5 = new String[]{"WAIT_PAY", "CLOSED"};
         List allow = CollectionsKt.listOf(var5);
         if (!allow.contains(entity.getStatus())) {
            return true;
         } else if (!Intrinsics.areEqual(content.get("state"), "success")) {
            this.onCancel(entity);
            Unit it = Unit.INSTANCE;
            int var7 = false;
            return true;
         } else {
            String var12 = (String)content.get("trade_out");
            if (var12 == null) {
               var12 = "";
            }

            String tradeOut = var12;
            var12 = (String)content.get("total_fee");
            int totalFee = var12 != null ? Integer.parseInt(var12) : 0;
            if (totalFee != entity.getTotalFee()) {
               throw new RuntimeError("实付金额与应付金额不一致！", 0, 2, (DefaultConstructorMarker)null);
            } else {
               this.onPayment(entity, tradeOut);
               Unit it = Unit.INSTANCE;
               int var9 = false;
               return true;
            }
         }
      }
   }

   public void onCancel(@NotNull OrderEntity entity) {
      Intrinsics.checkNotNullParameter(entity, "entity");
      Iterator var2 = this.getRepository().multiple(entity).iterator();

      while(var2.hasNext()) {
         OrderEntity detail = (OrderEntity)var2.next();
         ProductCancelDTO params = new ProductCancelDTO();
         params.setSubject(detail.getSubject());
         params.setTradeUnion(detail.getTradeUnion());
         this.getProds().cancel(params);
         detail.setStatus("CLOSED");
         this.getRepository().entity(detail);
      }

      if (Intrinsics.areEqual(entity.getType(), "multiple")) {
         ProductCancelDTO params = new ProductCancelDTO();
         params.setSubject(entity.getSubject());
         params.setTradeUnion(entity.getTradeNo());
         this.getProds().cancel(params);
         entity.setStatus("CLOSED");
         this.getRepository().entity(entity);
      }

   }

   private void onPayment(OrderEntity entity, String tradeOut) {
      List details = this.getRepository().multiple(entity);
      String tradeUnion = ((OrderEntity)CollectionsKt.first(details)).getTradeUnion();
      ProductPaymentDTO params = new ProductPaymentDTO();
      Iterable $this$map$iv = (Iterable)details;
      int $i$f$map = false;
      Collection destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10)));
      int $i$f$mapTo = false;
      Iterator var11 = $this$map$iv.iterator();

      while(var11.hasNext()) {
         Object item$iv$iv = var11.next();
         OrderEntity it = (OrderEntity)item$iv$iv;
         int var14 = false;
         OrderBuildRaw detail = new OrderBuildRaw();
         detail.setSubject(it.getSubject());
         detail.setTradeNo(it.getTradeNo());
         destination$iv$iv.add(detail);
      }

      params.setDetails((List)destination$iv$iv);
      params.setTradeUnion(tradeUnion);
      ProductResultStatus result = this.getProds().payment(params);
      String status = Intrinsics.areEqual(result.getState(), "NEXT") ? "WAIT_CONFIRM" : result.getState();
      String[] var20 = new String[]{"WAIT_CONFIRM", "WAIT_PROVIDE", "WAIT_RECEIPT", "WAIT_COMMENT"};
      List support = CollectionsKt.listOf(var20);
      if (!support.contains(status)) {
         throw new RuntimeError("非法请求�?", 0, 2, (DefaultConstructorMarker)null);
      } else {
         Iterator var21 = details.iterator();

         while(var21.hasNext()) {
            OrderEntity detail = (OrderEntity)var21.next();
            detail.setStatus(status);
            detail.setTradeOut(tradeOut);
            detail.setTotalPayment(detail.getTotalFee());
            detail.setPaymentAt(LocalDateTime.now());
            var11 = result.getProcesses().iterator();

            while(var11.hasNext()) {
               ProductResultParam process = (ProductResultParam)var11.next();
               if (Intrinsics.areEqual(detail.getTradeNo(), process.getTradeNo())) {
                  Map var10001 = detail.getProperty();
                  Map var10002 = process.getProperty();
                  if (var10002 == null) {
                     var10002 = MapsKt.emptyMap();
                  }

                  detail.setProperty(MapsKt.plus(var10001, var10002));
               }
            }

            this.getReports().pay(detail);
            this.getRepository().entity(detail);
         }

         if (Intrinsics.areEqual(entity.getType(), "multiple")) {
            entity.setStatus(status);
            entity.setTradeOut(tradeOut);
            entity.setTotalPayment(entity.getTotalFee());
            entity.setPaymentAt(LocalDateTime.now());
            this.getRepository().entity(entity);
         }

      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   public boolean receipt(@NotNull String tradeNo) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      OrderEntity var10000 = this.getRepository().entity(tradeNo);
      if (var10000 == null) {
         throw new RuntimeError("非法请求，不存在的订单！", 0, 2, (DefaultConstructorMarker)null);
      } else {
         OrderEntity entity = var10000;
         if (!Intrinsics.areEqual(entity.getStatus(), "WAIT_RECEIPT")) {
            throw new RuntimeError("订单状态异常，请刷新后再试�?", 0, 2, (DefaultConstructorMarker)null);
         } else {
            entity.setStatus("WAIT_COMMENT");
            this.getRepository().entity(entity);
            this.getReports().receipt(entity);
            return true;
         }
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   @NotNull
   public ResultEntity async(@NotNull String tradeNo, @NotNull Map body) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      Intrinsics.checkNotNullParameter(body, "body");
      OrderEntity entity = this.getRepository().entity(tradeNo);
      if (entity == null) {
         return new ResultEntity((Object)null, 0, (String)null, 7, (DefaultConstructorMarker)null);
      } else {
         if (Intrinsics.areEqual(body.get("provider"), "ticket")) {
            Object var10000 = entity.getProperty().get("ticket_info");
            Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type kotlin.collections.Map<*, *>");
            Map tickets = (Map)var10000;
            var10000 = body.get("ticket_item");
            Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type kotlin.collections.Map<*, *>");
            Map updated = (Map)var10000;
            var10000 = tickets.get("codes");
            Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type kotlin.collections.List<*>");
            Iterable $this$map$iv = (Iterable)((List)var10000);
            int $i$f$map = false;
            Collection destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10)));
            int $i$f$mapTo = false;
            Iterator var12 = $this$map$iv.iterator();

            while(var12.hasNext()) {
               Object item$iv$iv = var12.next();
               int var15 = false;
               Intrinsics.checkNotNull(item$iv$iv, "null cannot be cast to non-null type kotlin.collections.Map<*, *>");
               Map row = (Map)item$iv$iv;
               destination$iv$iv.add(Intrinsics.areEqual(row.get("id"), updated.get("id")) ? updated : (Map)item$iv$iv);
            }

            List codes = (List)destination$iv$iv;
            entity.setProperty(MapsKt.plus(entity.getProperty(), MapsKt.mapOf(TuplesKt.to("ticket_info", MapsKt.mapOf(TuplesKt.to("codes", codes))))));
            this.getRepository().entity(entity);
         }

         return new ResultEntity((Object)null, 0, (String)null, 7, (DefaultConstructorMarker)null);
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   @NotNull
   public ResultEntity confirm(@NotNull String tradeNo) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      OrderEntity entity = this.getRepository().entity(tradeNo);
      if (entity == null) {
         return new ResultEntity("NONE", 0, (String)null, 6, (DefaultConstructorMarker)null);
      } else if (Intrinsics.areEqual(entity.getType(), "multiple")) {
         throw new RuntimeError("无效的订单号�?", 0, 2, (DefaultConstructorMarker)null);
      } else if (!Intrinsics.areEqual(entity.getStatus(), "WAIT_CONFIRM")) {
         throw new RuntimeError("订单状态异常，请刷新后再试�?", 0, 2, (DefaultConstructorMarker)null);
      } else {
         entity.setStatus("WAIT_PROVIDE");
         this.getRepository().entity(entity);
         return new ResultEntity(entity.getStatus(), 0, (String)null, 6, (DefaultConstructorMarker)null);
      }
   }

   @Transactional(
      isolation = Isolation.SERIALIZABLE
   )
   @NotNull
   public OrderDetailResult provide(@NotNull String tradeNo, @NotNull Map body) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      Intrinsics.checkNotNullParameter(body, "body");
      OrderEntity order = this.getRepository().entity(tradeNo);
      if (order == null) {
         return new OrderDetailResult();
      } else if (!Intrinsics.areEqual(order.getStatus(), "WAIT_PROVIDE")) {
         throw new RuntimeError("订单状态异常，请刷新后再试�?", 0, 2, (DefaultConstructorMarker)null);
      } else {
         Object express = body.get("express_info");
         if (express instanceof Map && Intrinsics.areEqual(((Map)express).get("type"), "express")) {
            Object code = ((Map)express).get("code");
            this.getProds().express(String.valueOf(code));
         }

         order.setProperty(MapsKt.plus(order.getProperty(), body));
         order.setStatus("WAIT_RECEIPT");
         Convertible $this$convert$iv = (Convertible)this.getRepository().entity(order);
         int $i$f$convert = false;
         return (OrderDetailResult)ConvertibleKt.convert($this$convert$iv, Reflection.getOrCreateKotlinClass(OrderDetailResult.class));
      }
   }

   @NotNull
   public String comment(@NotNull String tradeNo) {
      Intrinsics.checkNotNullParameter(tradeNo, "tradeNo");
      OrderEntity entity = this.getRepository().entity(tradeNo);
      if (entity == null) {
         return "NONE";
      } else if (Intrinsics.areEqual(entity.getType(), "multiple")) {
         throw new RuntimeError("订单无法评价，请刷新后再试！", 0, 2, (DefaultConstructorMarker)null);
      } else if (!Intrinsics.areEqual(entity.getStatus(), "WAIT_COMMENT")) {
         return entity.getStatus();
      } else {
         entity.setStatus("FINISHED");
         this.getRepository().entity(entity);
         return entity.getStatus();
      }
   }
}
